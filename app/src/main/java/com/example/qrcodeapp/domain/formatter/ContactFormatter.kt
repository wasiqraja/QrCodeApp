package com.example.qrcodeapp.domain.formatter

import com.example.qrcodeapp.data.dto.QrType
import com.example.qrcodeapp.domain.model.ValidationResult
import com.example.qrcodeapp.domain.repository.BarcodeFormatter

class ContactFormatter : BarcodeFormatter {
    override val type = QrType.Contact
    override fun format(input: Map<String, String>): String {
        return "BEGIN:VCARD\nVERSION:2.1\n" +
                "N:${input["lName"]};${input["fName"]}\n" +
                "TEL:${input["phone"]}\n" +
                "EMAIL:${input["email"]}\nEND:VCARD"
    }

    override fun validate(input: Map<String, String>): ValidationResult {
        return if (input["fName"].isNullOrBlank()) ValidationResult.Invalid("First name required")
        else ValidationResult.Valid("valid")
    }
}