package com.example.qrcodeapp.data.repository

import com.example.qrcodeapp.data.local.dao.HistoryDao
import com.example.qrcodeapp.data.mapper.toDomain
import com.example.qrcodeapp.data.mapper.toEntity
import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepositoryImpl(
    private val dao: HistoryDao
) : HistoryRepository {

    override suspend fun saveHistory(history: History) {
        dao.insertHistory(history.toEntity())
    }

    override fun getAllHistory(): Flow<List<History>> {
        return dao.getAllHistory().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun deleteHistory(history: History) {
        dao.deleteHistory(history.toEntity())
    }

    override suspend fun deleteAllHistory() {
        dao.deleteAllHistory()
    }
}