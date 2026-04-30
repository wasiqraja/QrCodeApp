package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.repository.HistoryRepository

class FavouriteUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(id: Int, isFav: Boolean) = repository.updateFavourite(id, isFav)
}