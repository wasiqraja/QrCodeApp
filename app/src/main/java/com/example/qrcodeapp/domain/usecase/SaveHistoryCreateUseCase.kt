package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.domain.repository.HistoryCreateRepository
import com.example.qrcodeapp.domain.repository.HistoryRepository

class SaveHistoryCreateUseCase(
    private val repository: HistoryCreateRepository
) {
    suspend operator fun invoke(resId: Int,data: String,dateCreated: Long) {
        // validation logic lives here — not in ViewModel!
        require(resId != -1) { "Image cannot be empty" }
        require(data.isNotBlank()) { "data cannot be empty" }
        require(dateCreated.toString().isNotBlank()) { "dateCreated cannot be empty" }

        repository.saveHistoryCreate(
            History(imageRes = resId, data = data, createdAt = dateCreated)
        )
    }
}