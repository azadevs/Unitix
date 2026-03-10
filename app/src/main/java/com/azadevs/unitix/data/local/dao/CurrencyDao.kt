package com.azadevs.unitix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azadevs.unitix.data.local.entity.CurrencyRateEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/26
 */
@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<CurrencyRateEntity>)

    @Query("SELECT * FROM currency_rates")
    fun getAllRatesFlow(): Flow<List<CurrencyRateEntity>>

    @Query("SELECT * FROM currency_rates")
    suspend fun getAllRates(): List<CurrencyRateEntity>

    @Query("SELECT COUNT(*) FROM currency_rates")
    suspend fun getRatesCount(): Int
}
