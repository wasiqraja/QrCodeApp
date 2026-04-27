package com.example.qrcodeapp.data.repository

import android.graphics.Bitmap
import com.example.qrcodeapp.data.dto.QrType
import com.example.qrcodeapp.domain.model.BarCodeModel
import com.example.qrcodeapp.domain.repository.GenerateBarCodeRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.EnumMap

class BarCodeGenerateRepositoryImpl: GenerateBarCodeRepository {
    override suspend fun generateBarCode(barCodeModel: BarCodeModel): Bitmap? {
        return createQR(barCodeModel)
    }

    suspend fun createQR(
        barCodeModel: BarCodeModel
    ): Bitmap? {
        var finalBitmap: Bitmap? = null
        withContext(Dispatchers.IO) {
            val bitMatrix: BitMatrix
            val barcodeEncoder: BarcodeEncoder
            try {

                barCodeModel.run {
                    val multiFormatWriter = MultiFormatWriter()
                    val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
                    hints[EncodeHintType.MARGIN] = Integer.valueOf(2) /* default = 4 */
                    bitMatrix =
                        multiFormatWriter.encode(
                            value,
                            getBarcodeFormat(type),
                            width,
                            height,
                            hints
                        )

                    barcodeEncoder = BarcodeEncoder()
                    finalBitmap = barcodeEncoder.createBitmap(bitMatrix)

                }

            } catch (e: Exception) {
                return@withContext null
            }
        }
        return finalBitmap
    }

    fun getBarcodeFormat(type: String?): BarcodeFormat {
        return when (type) {
            QrType.DataMatrix.name -> BarcodeFormat.DATA_MATRIX
            QrType.PDF417.name -> BarcodeFormat.PDF_417
            QrType.Aztec.name -> BarcodeFormat.AZTEC
            QrType.EAN13.name -> BarcodeFormat.EAN_13
            QrType.EAN8.name -> BarcodeFormat.EAN_8
            QrType.UPCE.name -> BarcodeFormat.UPC_E
            QrType.UPCA.name -> BarcodeFormat.UPC_A
            QrType.Code128.name -> BarcodeFormat.CODE_128
            QrType.Code93.name -> BarcodeFormat.CODE_93
            QrType.Code39.name -> BarcodeFormat.CODE_39
            QrType.CodaBar.name -> BarcodeFormat.CODABAR
            QrType.ITF.name -> BarcodeFormat.ITF
            else -> BarcodeFormat.QR_CODE
        }
    }

}