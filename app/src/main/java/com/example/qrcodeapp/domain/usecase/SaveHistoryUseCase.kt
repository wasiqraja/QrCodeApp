package com.example.qrcodeapp.domain.usecase

import androidx.savedstate.savedState
import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.domain.repository.HistoryRepository

class SaveHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(resId: Int, data: String, dateCreated: Long) {
        // validation logic lives here — not in ViewModel!
        require(resId != -1) { "Image cannot be empty" }
        require(data.isNotBlank()) { "data cannot be empty" }
        require(dateCreated.toString().isNotBlank()) { "dateCreated cannot be empty" }


        repository.saveHistory(
            History(imageRes = resId, data = data, createdAt = dateCreated)
        )

    }
}