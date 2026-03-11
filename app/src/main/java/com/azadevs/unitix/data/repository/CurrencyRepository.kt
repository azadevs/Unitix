package com.azadevs.unitix.data.repository

import android.util.Log
import com.azadevs.unitix.data.local.dao.CurrencyDao
import com.azadevs.unitix.data.local.entity.CurrencyRateEntity
import com.azadevs.unitix.data.network.CurrencyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/26
 */
class CurrencyRepository(
    private val api: CurrencyApi,
    private val dao: CurrencyDao
) {

    fun getCurrencyRates(): Flow<Resource<List<CurrencyRateEntity>>> = flow {
        emit(Resource.Loading())
        val cachedRates = dao.getAllRates()
        if (cachedRates.isNotEmpty()) {
            emit(Resource.Success(cachedRates))
        }
        try {
            val response = api.getLatestRates()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val lastUpdated = System.currentTimeMillis()

                val entities = body.rates.map { (code, rate) ->
                    CurrencyRateEntity(
                        currencyCode = code,
                        rateRelativeToUSD = rate,
                        lastUpdatedMillis = lastUpdated
                    )
                }
                dao.insertRates(entities)

                emit(Resource.Success(entities))
            } else {
                if (cachedRates.isEmpty()) {
                    emit(Resource.Error("Error: Failed to connect to API."))
                }
            }
        } catch (e: Exception) {
            Log.e("CurrencyRepository", "Network error", e)
            if (cachedRates.isEmpty()) {
                emit(Resource.Error("No internet connection. Please connect to a network."))
            }
        }
    }.flowOn(Dispatchers.IO)

    fun searchRatesLocally(): Flow<List<CurrencyRateEntity>> = dao.getAllRatesFlow()
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
