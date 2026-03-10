package com.azadevs.unitix.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.azadevs.unitix.data.local.dao.CurrencyDao
import com.azadevs.unitix.data.local.dao.HistoryDao
import com.azadevs.unitix.data.local.entity.CurrencyRateEntity
import com.azadevs.unitix.data.local.entity.HistoryItemEntity

@Database(
    entities = [HistoryItemEntity::class, CurrencyRateEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(StringConverters::class)
abstract class UnitixDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun currencyDao(): CurrencyDao

    companion object {
        @Volatile
        private var INSTANCE: UnitixDatabase? = null

        fun getDatabase(context: Context): UnitixDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UnitixDatabase::class.java,
                    "unitix_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
