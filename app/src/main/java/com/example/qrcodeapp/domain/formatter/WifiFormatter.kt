package com.example.qrcodeapp.domain.formatter

import com.example.qrcodeapp.data.dto.QrType
import com.example.qrcodeapp.domain.model.ValidationResult
import com.example.qrcodeapp.domain.repository.BarcodeFormatter

class WifiFormatter : BarcodeFormatter {
    override val type = QrType.WiFi
    override fun format(input: Map<String, String>): String {
        val ssid = input["ssid"] ?: ""
        val pass = input["password"] ?: ""
        val security = input["security"] ?: "nopass"
        return "WIFI:T:$security;S:$ssid;P:$pass;;"
    }

    override fun validate(input: Map<String, String>): ValidationResult {
        return if (input["ssid"].isNullOrBlank()) {
            ValidationResult.Invalid("error")
        } else ValidationResult.Valid("valid")
    }
}