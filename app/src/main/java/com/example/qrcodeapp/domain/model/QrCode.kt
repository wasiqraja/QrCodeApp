package com.example.qrcodeapp.domain.model

data class QrCode(
    val id: Int? = null,
    val name: String,
    val icon: Int,
    var isQrCode: Boolean = true,
    val isPremium: Boolean = false,
    var strName: String = ""
)
