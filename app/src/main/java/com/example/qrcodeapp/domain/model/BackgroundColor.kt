package com.example.qrcodeapp.domain.model

data class BackgroundColor(
    var solidColor: String? = null,
    var gradientColor: Gradient? = null,
    var imageDrawable: Int? = null,
    var imageUri: String? = null
)