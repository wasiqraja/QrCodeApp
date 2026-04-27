package com.example.qrcodeapp.domain.model

import com.example.qrcodeapp.core.utils.model.Dots
import com.example.qrcodeapp.core.utils.model.Eyes
import com.github.alexzhirkevich.customqrgenerator.QrOptions

data class TemplateResult(
    val qrOptions: QrOptions,
    val selectedDots: Dots,
    val selectedEyes: Eyes,
    val templateName: String = ""
)