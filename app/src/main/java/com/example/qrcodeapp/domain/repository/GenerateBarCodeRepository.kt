package com.example.qrcodeapp.domain.repository

import android.graphics.Bitmap
import com.example.qrcodeapp.domain.model.BarCodeModel

interface GenerateBarCodeRepository {
    suspend fun generateBarCode(barCodeModel: BarCodeModel): Bitmap?
}