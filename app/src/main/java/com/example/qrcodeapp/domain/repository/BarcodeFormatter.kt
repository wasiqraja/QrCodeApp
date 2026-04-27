package com.example.qrcodeapp.domain.repository

import com.example.qrcodeapp.data.dto.QrType
import com.example.qrcodeapp.domain.model.ValidationResult

interface BarcodeFormatter {
    val type: QrType
    fun format(input: Map<String, String>): String
    fun validate(input: Map<String, String>): ValidationResult
}