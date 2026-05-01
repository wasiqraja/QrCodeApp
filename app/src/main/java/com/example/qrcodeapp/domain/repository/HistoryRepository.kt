package com.example.qrcodeapp.domain.repository

import com.example.qrcodeapp.domain.model.History
import kotlinx.coroutines.flow.Flow

// Contract — domain has NO idea about Room!
interface HistoryRepository {
    suspend fun saveHistory(history: History) : Long
    fun getAllHistory(): Flow<List<History>>
    suspend fun deleteHistory(lastId: Long)
    suspend fun deleteSelectedHistory(list: List<Int>)
    suspend fun deleteAllHistory()

    suspend fun updateFavourite(id: Int, isFav: Boolean): Boolean
}
