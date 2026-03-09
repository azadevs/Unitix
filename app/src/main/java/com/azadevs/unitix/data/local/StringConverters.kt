package com.azadevs.unitix.data.local

import androidx.room.TypeConverter
import com.azadevs.unitix.data.model.Category
import com.azadevs.unitix.data.model.UnitType

class StringConverters {
    @TypeConverter
    fun fromUnitType(value: UnitType): String {
        return value.name
    }

    @TypeConverter
    fun toUnitType(value: String): UnitType {
        return try {
            UnitType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            UnitType.METER
        }
    }

    @TypeConverter
    fun fromCategory(value: Category): String {
        return value.name
    }

    @TypeConverter
    fun toCategory(value: String): Category {
        return try {
            Category.valueOf(value)
        } catch (e: IllegalArgumentException) {
            Category.LENGTH
        }
    }
}
