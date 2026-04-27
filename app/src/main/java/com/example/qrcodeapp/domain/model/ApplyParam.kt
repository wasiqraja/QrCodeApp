package com.example.qrcodeapp.domain.model

import com.example.qrcodeapp.core.utils.model.Dots
import com.example.qrcodeapp.core.utils.model.Eyes

data class ApplyParam(
    val logoImage: Int = -1,
    val dotsShape: Dots = Dots.DEFAULT,
    val eyesShape: Eyes = Eyes.SQUARE_RHOMBUS,
    val foreGroundColor: ForegroundColor? = null,
    val backgroundColor: BackgroundColor? = null,
    val logoUri: String? = null
)
