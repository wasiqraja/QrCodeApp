package com.example.qrcodeapp.data.repository

import com.example.qrcodeapp.data.local.dao.HistoryDao
import com.example.qrcodeapp.data.mapper.toDomain
import com.example.qrcodeapp.data.mapper.toEntity
import com.example.qrcodeapp.data.mapper.toEntityCreate
import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.domain.repository.HistoryCreateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryCreateRepositoryImpl(
    private val dao: HistoryDao
) : HistoryCreateRepository {

    override suspend fun saveHistoryCreate(history: History) {
        dao.insertHistoryCreate(history.toEntityCreate())
    }

    override fun getAllHistoryCreate(): Flow<List<History>> {
        return dao.getAllHistoryCreate().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun deleteHistoryCreate(history: History) {
        dao.deleteHistoryCreate(history.toEntityCreate())
    }

    override suspend fun deleteAllHistoryCreate() {
        dao.deleteAllHistoryCreate()
    }
}