package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.domain.repository.HistoryRepository

class SaveHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(history: History) = repository.saveHistory(history)

        /*       require(resId != -1) { "Image cannot be empty" }
               require(data.isNotBlank()) { "data cannot be empty" }
               require(dateCreated.toString().isNotBlank()) { "dateCreated cannot be empty" }*/





}