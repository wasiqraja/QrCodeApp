package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.model.CameraModel
import com.example.qrcodeapp.domain.repository.CameraRepository

class CameraUseCase(val cameraRepository: CameraRepository) {
    suspend operator fun invoke(cameraModel: CameraModel) =
        cameraRepository.startCamera(cameraModel)
}