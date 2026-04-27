package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.domain.repository.HistoryCreateRepository
import kotlinx.coroutines.flow.Flow

class FetchHistoryCreateUseCase(
    private val repository: HistoryCreateRepository
) {
    operator fun invoke(): Flow<List<History>> {
        return repository.getAllHistoryCreate()
    }
}
