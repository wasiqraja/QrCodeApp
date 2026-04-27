package com.example.qrcodeapp.domain.repository

import com.example.qrcodeapp.domain.model.History
import kotlinx.coroutines.flow.Flow

// Contract — domain has NO idea about Room!
interface HistoryCreateRepository {
    suspend fun saveHistoryCreate(history: History)
    fun getAllHistoryCreate(): Flow<List<History>>
    suspend fun deleteHistoryCreate(history: History)
    suspend fun deleteAllHistoryCreate()
}