package com.example.qrcodeapp.domain.model


data class History(
    val id: Int = 0,
    val originalString: String,       // the input text you passed
    val resultedText: String,         // any processed/display text
    val type: String,                 // "QR" or "BARCODE"
    val dateString: String,
    val isFavourite: Boolean
)

