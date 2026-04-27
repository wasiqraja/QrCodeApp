package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow

class FetchHistoryUseCase(
    private val repository: HistoryRepository
) {
    operator fun invoke(): Flow<List<History>> {
        return repository.getAllHistory()
    }
}
