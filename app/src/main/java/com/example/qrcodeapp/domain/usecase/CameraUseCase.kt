package com.example.qrcodeapp.domain.usecase

import com.example.qrcodeapp.domain.model.CameraModel
import com.example.qrcodeapp.domain.repository.CameraRepository

class CameraUseCase(val cameraRepository: CameraRepository) {
    suspend operator fun invoke(cameraModel: CameraModel) =
        cameraRepository.startCamera(cameraModel)

    suspend fun takeImage(cameraModel: CameraModel)=cameraRepository.takeImage(cameraModel)
    suspend fun initCamera(cameraModel: CameraModel)=cameraRepository.intCamera(cameraModel)

}