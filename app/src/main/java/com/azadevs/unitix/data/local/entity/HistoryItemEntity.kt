package com.azadevs.unitix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.azadevs.unitix.data.model.Category
import com.azadevs.unitix.data.model.UnitType

@Entity(tableName = "history_items")
data class HistoryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fromValue: String,
    val fromUnit: UnitType,
    val toValue: String,
    val toUnit: UnitType,
    val category: Category,
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
