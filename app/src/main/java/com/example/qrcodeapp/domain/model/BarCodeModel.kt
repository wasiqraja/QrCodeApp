package com.example.qrcodeapp.domain.model

data class BarCodeModel(
    val value: String?=null,
    val type: String?=null,
    val width: Int = 800,
    val height: Int = 800,
)
