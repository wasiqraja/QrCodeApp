package com.example.qrcodeapp.domain.model

data class Gradient(
    var startColor: String?,
    var endColor: String?,
    var drawable: Int?,
    var centreColor:String? = null
)