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

    override suspend fun saveHistory(history: History): Long {
        return dao.insertHistory(history.toEntity())
    }

    override fun getAllHistory(): Flow<List<History>> {
        return dao.getAllHistory().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun deleteHistory(lastId: Long) {
        dao.deleteHistoryById(lastId)
    }

    override suspend fun deleteAllHistory() {
        dao.deleteAllHistory()
    }

    override suspend fun updateFavourite(id: Int, isFav: Boolean): Boolean {
        dao.updateFavourite(id)
        return dao.getFavouriteStatus(id)
    }
}