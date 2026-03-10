package com.azadevs.unitix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/26
 */
@Entity(tableName = "currency_rates")
data class CurrencyRateEntity(
    @PrimaryKey
    val currencyCode: String,
    val rateRelativeToUSD: Double,
    val lastUpdatedMillis: Long
)
