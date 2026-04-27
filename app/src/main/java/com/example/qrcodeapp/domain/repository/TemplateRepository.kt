package com.example.qrcodeapp.domain.repository

import com.example.qrcodeapp.domain.model.TemplateResult
import com.example.qrcodeapp.domain.model.ApplyParam

interface TemplateRepository {

    //aka color
    fun applyTemplate(
        applyParam: ApplyParam
    ): TemplateResult

    fun applyGradient(
        applyParam: ApplyParam
    ): TemplateResult

    fun applyPicture(
        applyParam: ApplyParam
    ): TemplateResult

    fun applyLogo(
        applyParam: ApplyParam
    ): TemplateResult
}