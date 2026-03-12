package com.azadevs.unitix.features.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azadevs.unitix.data.repository.CurrencyRepository
import com.azadevs.unitix.data.repository.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/26
 */
data class CurrencyUiModel(
    val code: String,
    val rate: Double,
    val trendPercentage: Double,
    val isUp: Boolean
)

data class CurrencyUiState(
    val rates: List<CurrencyUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val baseCurrency: String = "USD"
)

class CurrencyViewModel(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        loadRates()
    }

    private fun loadRates() {
        repository.getCurrencyRates().onEach { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                }

                is Resource.Success -> {
                    val models = resource.data?.map { entity ->
                        val (trend, isUp) = generateTrend(entity.currencyCode)
                        CurrencyUiModel(
                            code = entity.currencyCode,
                            rate = entity.rateRelativeToUSD,
                            trendPercentage = trend,
                            isUp = isUp
                        )
                    } ?: emptyList()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        rates = models.sortedBy { it.code }
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = resource.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onBaseCurrencySelected(currencyCode: String) {
        _uiState.value = _uiState.value.copy(baseCurrency = currencyCode)
    }

    private fun generateTrend(currencyCode: String): Pair<Double, Boolean> {
        // Pseudo-random but stable for the day
        val daySeed = System.currentTimeMillis() / (1000 * 60 * 60 * 24)
        val stringSeed = currencyCode.hashCode().toLong()
        val combinedSeed = daySeed + stringSeed
        val random = java.util.Random(combinedSeed)

        // Exclude USD since it's the base
        if (currencyCode == "USD") return Pair(0.0, true)

        val trend = random.nextDouble() * 2.5 // Up to 2.5% change
        val isUp = random.nextBoolean()
        return Pair(trend, isUp)
    }
}

class CurrencyViewModelFactory(
    private val repository: CurrencyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CurrencyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
