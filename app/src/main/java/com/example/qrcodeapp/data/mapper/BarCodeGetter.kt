package com.example.qrcodeapp.data.mapper

import com.example.qrcodeapp.R
import com.example.qrcodeapp.data.dto.QrType
import com.example.qrcodeapp.domain.model.QrCode

class BarCodeGetter() {

    var barCoderList: MutableList<QrCode> = mutableListOf()

    fun getBarCodeList(): List<QrCode> {
        barCoderList.clear()
        barCoderList.add(QrCode(null, QrType.DataMatrix.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.PDF417.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.Aztec.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.EAN13.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.EAN8.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.UPCE.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.UPCA.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.Code128.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.Code93.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.Code39.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.CodaBar.name, R.drawable.ic_barcode, false))
        barCoderList.add(QrCode(null, QrType.ITF.name, R.drawable.ic_barcode, false))
        return barCoderList
    }

}