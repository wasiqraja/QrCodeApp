package com.example.qrcodeapp.data.repository

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.example.qrcodeapp.core.utils.Constants.isWebsite
import com.example.qrcodeapp.core.utils.ImageUtils
import com.example.qrcodeapp.core.utils.custom.CustomQRViewJava
import com.example.qrcodeapp.core.utils.logEvent
import com.example.qrcodeapp.domain.model.CameraModel
import com.example.qrcodeapp.domain.repository.CameraRepository
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.math.abs

class CameraRepositoryImpl : CameraRepository {
    /*  override suspend fun startCamera(cameraModel: CameraModel): String? {
          startCameraXCompose(cameraModel){ res->
             return@startCameraXCompose res
         }
      }*/

    override suspend fun startCamera(cameraModel: CameraModel): String? =
        suspendCancellableCoroutine { continuation ->
            startCameraXCompose(cameraModel) { res ->
                if (continuation.isActive) {
                    continuation.resume(res)
                }
            }
            continuation.invokeOnCancellation {
            }
        }


    @OptIn(ExperimentalGetImage::class)
    private fun startCameraXCompose(
        cameraModel: CameraModel, onResult: (String?) -> Unit
    ) {


        cameraModel.run {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            val cameraExecutor = Executors.newSingleThreadExecutor()
            val scanner: BarcodeScanner by lazy { BarcodeScanning.getClient() }

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // 1. Preview
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                // 2. Image Analysis
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                    val image: Image = imageProxy.image!!

                    val rectangle = viewFinder.frameRect
                    val bitmap = ImageUtils.convertYuv420888ImageToBitmap(image) {}

                    val bitmapCropped = rectangle?.let {
                        getCroppedBitmap(bitmap, it, viewFinder)
                    }

                    bitmap.recycle()

                    if (bitmapCropped != null) {
                        val inp = InputImage.fromBitmap(bitmapCropped, 180)
                        scanner.process(inp).addOnSuccessListener { barcode ->
                            Log.d("TAG", "startCameraXdbug:::barcode====$barcode: ")

                            bitmapCropped.recycle()
                            if (barcode.isNotEmpty()) {
                                checkScannedBarcode(barcode[0])
                                onResult(barcode[0].displayValue)
                                imageProxy.close()
                            } else imageProxy.close()
                        }.addOnFailureListener {

                            bitmapCropped.recycle()
                            imageProxy.close()
                        }
                    }
                }

                // 3. Bind to Lifecycle
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.e("CameraX", "Binding failed", e)
                }
            }, ContextCompat.getMainExecutor(context))
        }

    }


    private fun getCroppedBitmap(
        bitmap: Bitmap,
        rectangle: com.example.qrcodeapp.core.utils.custom.Rect,
        viewFinder: CustomQRViewJava
    ): Bitmap {
        val output = createBitmap(bitmap.width, bitmap.height)
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color

        val screenWidth = viewFinder.measuredWidth
        val constant: Float =
            if (screenWidth < 720) 1.5f else if (screenWidth < 1000) 1.7f else if (screenWidth < 1400) 2f else 2.4f

        val radius = abs(rectangle.left / constant - rectangle.right / constant)

        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(), radius / constant, paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect.left.toFloat(), rect.top.toFloat(), paint)
        return output
    }

    private fun checkScannedBarcode(code: Barcode, fromGallery: Boolean = false) {

        if ((code.displayValue?.isWebsite() == true || code.valueType == 5)) {
            val isBarcode = code.valueType == 5
            val url =
                if (code.valueType == 5) "https://www.google.com/search?q=${code.displayValue}"
                else code.displayValue
            logEvent("r=> ${isBarcode} ${url}")
            //Log.d("SCANNER", "${isBarcode} ${url}")

        }
    }
}