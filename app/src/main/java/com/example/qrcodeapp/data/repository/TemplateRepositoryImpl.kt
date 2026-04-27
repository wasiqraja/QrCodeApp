package com.example.qrcodeapp.data.repository

import com.example.qrcodeapp.domain.model.TemplateResult
import com.example.qrcodeapp.data.mapper.QrTemplateProcessor
import com.example.qrcodeapp.domain.model.ApplyParam
import com.example.qrcodeapp.domain.repository.TemplateRepository

class TemplateRepositoryImpl(val qrTemplateProcessor: QrTemplateProcessor) : TemplateRepository {

    override fun applyTemplate(
        applyParam: ApplyParam
    ): TemplateResult {
        return qrTemplateProcessor.process(applyParam)
    }

    override fun applyGradient(applyParam: ApplyParam): TemplateResult {
        return qrTemplateProcessor.process(applyParam)
    }

    override fun applyPicture(applyParam: ApplyParam): TemplateResult {
        return qrTemplateProcessor.process(applyParam)
    }

    override fun applyLogo(applyParam: ApplyParam): TemplateResult {
        return qrTemplateProcessor.process(applyParam)
    }

}