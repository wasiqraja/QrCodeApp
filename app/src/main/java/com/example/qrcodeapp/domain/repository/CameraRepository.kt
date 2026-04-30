package com.example.qrcodeapp.domain.repository


import com.example.qrcodeapp.domain.model.CameraModel
import com.example.qrcodeapp.domain.model.PayLoadModel


interface CameraRepository {

    suspend fun intCamera(cameraModel: CameraModel): PayLoadModel
    suspend fun startCamera(cameraModel: CameraModel): PayLoadModel
    suspend fun takeImage(cameraModel: CameraModel): PayLoadModel
}