package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.model.BarCodeModel
import com.example.qrcodeapp.domain.repository.GenerateBarCodeRepository

class GenerateBarCodeUseCase(private val barCodeGenRepo: GenerateBarCodeRepository) {
    suspend operator fun invoke(barCodeModel: BarCodeModel) =
        barCodeGenRepo.generateBarCode(barCodeModel)
}