package com.example.qrcodeapp.domain.repository

import com.example.qrcodeapp.domain.model.History
import kotlinx.coroutines.flow.Flow

// Contract — domain has NO idea about Room!
interface HistoryRepository {
    suspend fun saveHistory(history: History)
    fun getAllHistory(): Flow<List<History>>
    suspend fun deleteHistory(history: History)
    suspend fun deleteAllHistory()
}