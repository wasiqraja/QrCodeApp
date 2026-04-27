package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.data.dto.QrType
import com.example.qrcodeapp.domain.model.ValidationResult
import com.example.qrcodeapp.domain.repository.BarcodeFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// domain/usecase/PrepareQrDataUseCase.kt
class PrepareQrDataUseCase(private val formatters: List<BarcodeFormatter>) {

    operator fun invoke(type: QrType, data: Map<String, String>): Flow<ValidationResult> = flow {
        val formatter = formatters.find { it.type == type }
            ?: throw IllegalArgumentException("No formatter for $type")

        when (val validation = formatter.validate(data)) {
            is ValidationResult.Valid -> {
                // Generate the actual formatted string (e.g., "WIFI:T:WPA...")
                val formattedData = formatter.format(data)
                emit(ValidationResult.Valid(formattedData))
            }

            is ValidationResult.Invalid -> {
                emit(ValidationResult.Invalid(validation.errorMessage))
            }
        }
    }
}