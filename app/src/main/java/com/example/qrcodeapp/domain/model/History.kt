package com.example.qrcodeapp.domain.model


data class History(
    val id: Int = 0,
    val data: String,
    val imageRes: Int,
    val createdAt: Long = System.currentTimeMillis()
)

