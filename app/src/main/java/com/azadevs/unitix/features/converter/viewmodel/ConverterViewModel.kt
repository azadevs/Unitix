package com.azadevs.unitix.features.converter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azadevs.unitix.data.engine.ConverterEngine
import com.azadevs.unitix.data.local.entity.HistoryItemEntity
import com.azadevs.unitix.data.model.Category
import com.azadevs.unitix.data.model.UnitType
import com.azadevs.unitix.data.repository.HistoryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.abs

/**
 * Created by : Azamat Kalmurzaev
 * 26/01/26
 */
class ConverterViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val engine = ConverterEngine()

    var inputText by mutableStateOf("")
        private set

    var fromUnit by mutableStateOf(UnitType.METER)
        private set

    var toUnit by mutableStateOf(UnitType.KILOMETER)
        private set
    var resultText by mutableStateOf("0")
        private set

    private var saveJob: Job? = null

    val allHistory = historyRepository.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteHistory = historyRepository.favoriteHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var isHistorySheetVisible by mutableStateOf(false)
        private set

    fun showHistorySheet(show: Boolean) {
        isHistorySheetVisible = show
    }

    fun toggleFavorite(item: HistoryItemEntity) {
        viewModelScope.launch {
            historyRepository.updateHistory(item.copy(isFavorite = !item.isFavorite))
        }
    }

    fun deleteHistoryItem(item: HistoryItemEntity) {
        viewModelScope.launch {
            historyRepository.deleteHistory(item.id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            historyRepository.clearAll()
        }
    }

    fun restoreFromHistory(item: HistoryItemEntity) {
        fromUnit = item.fromUnit
        toUnit = item.toUnit
        inputText = item.fromValue
        resultText = item.toValue
    }

    fun onInputChange(text: String) {
        inputText = text
        recalc()
    }

    fun onFromUnitChange(unit: UnitType) {
        fromUnit = unit
        recalc()
    }

    fun onToUnitChange(unit: UnitType) {
        toUnit = unit
        recalc()
    }

    private fun recalc() {
        if (inputText.isEmpty()) {
            resultText = "0"
            return
        }
        
        val parsedText = inputText.replace(',', '.')
        val value = parsedText.toDoubleOrNull()

        if (value == null) {
            resultText = "0"
            return
        }

        val result = engine.convert(value, fromUnit, toUnit)
        resultText = format(result)

        scheduleHistorySave(parsedText, resultText, fromUnit, toUnit)
    }

    private fun scheduleHistorySave(fromV: String, toV: String, fUnit: UnitType, tUnit: UnitType) {
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            delay(1500) // Debounce before auto-saving
            if (fromV.isNotEmpty() && toV != "0") {
                val category = getCategoryForUnit(fUnit)
                val entity = HistoryItemEntity(
                    fromValue = fromV,
                    fromUnit = fUnit,
                    toValue = toV,
                    toUnit = tUnit,
                    category = category
                )
                historyRepository.addHistory(entity)
            }
        }
    }

    private fun getCategoryForUnit(unit: UnitType): Category {
        val item = com.azadevs.unitix.data.model.UnitItem.units.find { it.type == unit }
        return item?.category ?: Category.LENGTH // fallback
    }

    private fun format(v: Double): String {
        val absVal = abs(v)

        return when (absVal) {
            0.0 -> "0"
            !in 0.0001..<1.0E9 -> {
                DecimalFormat("0.####E0").format(v)
            }

            else -> {
                val df = DecimalFormat("#,##0.####")
                df.format(v)
            }
        }
    }

    fun swapUnits() {
        val temp = fromUnit
        fromUnit = toUnit
        toUnit = temp
        recalc()
    }

    fun clearInput() {
        inputText = ""
        resultText = "0"
    }
}

class ConverterViewModelFactory(private val repository: HistoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConverterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConverterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
