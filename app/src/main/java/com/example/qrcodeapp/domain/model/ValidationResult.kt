package com.example.qrcodeapp.domain.model

sealed class ValidationResult {
    data class Valid(val data: String) : ValidationResult()
    data class Invalid(val errorMessage: String) : ValidationResult()
}