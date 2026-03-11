package com.azadevs.unitix.data.network

import com.azadevs.unitix.data.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/26
 */
interface CurrencyApi {
    @GET("v6/latest/USD")
    suspend fun getLatestRates(): Response<CurrencyResponse>

    companion object {
        const val BASE_URL = "https://open.er-api.com/"
    }
}
