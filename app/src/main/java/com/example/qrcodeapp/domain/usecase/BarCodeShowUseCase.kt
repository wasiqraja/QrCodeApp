package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.repository.BarCodeRepository

class BarCodeShowUseCase(val repository: BarCodeRepository) {
    operator fun invoke() = repository.getBarCode()
}