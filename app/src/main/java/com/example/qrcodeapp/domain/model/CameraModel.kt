package com.example.qrcodeapp.domain.model

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.qrcodeapp.core.utils.custom.CustomQRViewJava

data class CameraModel(
    val context: Context,
    val lifecycleOwner: LifecycleOwner,
    val previewView: PreviewView,
    val viewFinder: CustomQRViewJava,
    val cameraSelector: CameraSelector,
    val imageCapture: ImageCapture?=null,
    val imageAnalysis: ImageAnalysis?=null
)
