package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.repository.HistoryRepository

class DeleteHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(lastId:Long) {
        return repository.deleteHistory(lastId)
    }

    suspend operator fun invoke(list:List<Int>) {
        return repository.deleteSelectedHistory(list)
    }
}
