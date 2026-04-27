package com.example.qrcodeapp.domain.repository

import com.example.qrcodeapp.domain.model.QrCode

interface BarCodeRepository{
    fun getBarCode():List<QrCode>
}