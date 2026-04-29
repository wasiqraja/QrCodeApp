package com.example.qrcodeapp.domain.repository

import androidx.camera.core.Camera
import com.example.qrcodeapp.domain.model.CameraModel

interface CameraRepository {
    suspend fun startCamera(cameraModel: CameraModel): String?
}