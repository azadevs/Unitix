package com.azadevs.unitix.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.azadevs.unitix.data.local.dao.HistoryDao
import com.azadevs.unitix.data.local.entity.HistoryItemEntity

@Database(entities = [HistoryItemEntity::class], version = 1, exportSchema = false)
@TypeConverters(StringConverters::class)
abstract class UnitixDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: UnitixDatabase? = null

        fun getDatabase(context: Context): UnitixDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UnitixDatabase::class.java,
                    "unitix_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
