package com.example.qrcodeapp.data.repository

import com.example.qrcodeapp.data.mapper.BarCodeGetter
import com.example.qrcodeapp.domain.model.QrCode
import com.example.qrcodeapp.domain.repository.BarCodeRepository

class BarCodeRepositoryImpl(private val barCodeGetter: BarCodeGetter) : BarCodeRepository {
    override fun getBarCode(): List<QrCode> {
        return barCodeGetter.getBarCodeList()
    }
}