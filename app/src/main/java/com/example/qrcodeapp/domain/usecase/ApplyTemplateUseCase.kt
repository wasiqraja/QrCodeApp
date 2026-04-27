package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.model.ApplyParam
import com.example.qrcodeapp.domain.repository.TemplateRepository

class ApplyTemplateUseCase(val repository: TemplateRepository) {
    operator fun invoke(applyParam: ApplyParam) = repository.applyTemplate(applyParam)
}