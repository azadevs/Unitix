package com.azadevs.unitix.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/26
 */
data class CurrencyResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("time_last_update_unix")
    val timeLastUpdateUnix: Long,
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("rates")
    val rates: Map<String, Double>
)
