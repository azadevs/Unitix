package com.azadevs.unitix.data.repository

import com.azadevs.unitix.data.local.dao.HistoryDao
import com.azadevs.unitix.data.local.entity.HistoryItemEntity
import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val historyDao: HistoryDao) {

    val allHistory: Flow<List<HistoryItemEntity>> = historyDao.getAllHistory()

    val favoriteHistory: Flow<List<HistoryItemEntity>> = historyDao.getFavorites()

    suspend fun addHistory(item: HistoryItemEntity) {
        historyDao.insertHistory(item)
    }

    suspend fun updateHistory(item: HistoryItemEntity) {
        historyDao.updateHistory(item)
    }

    suspend fun deleteHistory(id: Int) {
        historyDao.deleteHistoryById(id)
    }

    suspend fun clearAll() {
        historyDao.clearAllHistory()
    }
}
