package com.example.qrcodeapp.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.example.qrcodeapp.core.utils.Constants.isWebsite
import com.example.qrcodeapp.core.utils.ImageUtils
import com.example.qrcodeapp.core.utils.custom.CustomQRViewJava
import com.example.qrcodeapp.core.utils.logEvent
import com.example.qrcodeapp.domain.model.CameraModel
import com.example.qrcodeapp.domain.model.PayLoadModel
import com.example.qrcodeapp.domain.repository.CameraRepository
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.math.abs

class CameraRepositoryImpl(context: Context) : CameraRepository {
    /*  override suspend fun startCamera(cameraModel: CameraModel): String? {
          startCameraXCompose(cameraModel){ res->
             return@startCameraXCompose res
         }
      }*/
    var camera: Camera? = null
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val cameraExecutor = Executors.newSingleThreadExecutor()
    val scanner: BarcodeScanner by lazy { BarcodeScanning.getClient() }

    init {

    }

    override suspend fun startCamera(cameraModel: CameraModel): PayLoadModel =
        suspendCancellableCoroutine { continuation ->
            startCameraXCompose(cameraModel) { res ->
                if (continuation.isActive) {
                    continuation.resume(res as PayLoadModel)
                }
            }
            continuation.invokeOnCancellation {
            }
        }

    override suspend fun takeImage(cameraModel: CameraModel): PayLoadModel =

        suspendCancellableCoroutine { continuation ->

            Log.d("TAGRD", "takeImage ")

            captureAndScan(cameraModel) { res ->
                if (continuation.isActive) {
                    continuation.resume(res as PayLoadModel)
                }
            }

            continuation.invokeOnCancellation {
            }
        }

    override suspend fun intCamera(cameraModel: CameraModel): PayLoadModel {

        cameraModel.run {

            cameraProviderFuture.addListener({

                val cameraProvider = cameraProviderFuture.get()
                // 1. Preview
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }


                // 3. Bind to Lifecycle
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture,
                        imageAnalysis
                    ).let {
                        camera = it
                    }
                } catch (e: Exception) {
                    Log.e("CameraX", "Binding failed", e)
                }
            }, ContextCompat.getMainExecutor(context))
        }

        return PayLoadModel("", camera)
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCameraXCompose(
        cameraModel: CameraModel, onResult: (PayLoadModel?) -> Unit
    ) {

        cameraModel.run {

            Log.d("TAGRD", "display value=> sf: ")

            imageAnalysis?.setAnalyzer(cameraExecutor) { imageProxy ->

                Log.d("TAGRD", "display value=> imageAnalysis: ")


                val image: Image = imageProxy.image!!

                val rectangle = viewFinder.frameRect
                val bitmap = ImageUtils.convertYuv420888ImageToBitmap(image) {}

                val bitmapCropped = rectangle?.let {
                    getCroppedBitmap(bitmap, it, viewFinder)
                }

                bitmap.recycle()

                Log.d("TAGRD", "display value=> sdfdsf ")

                if (bitmapCropped != null) {
                    val inp = InputImage.fromBitmap(bitmapCropped, 180)
                    scanner.process(inp).addOnSuccessListener { barcode ->

                        Log.d("TAGRD", "display value=> addOnSuccessListener ")
                        bitmapCropped.recycle()
                        if (barcode.isNotEmpty()) {
                            Log.d("TAGRD", "display value=> $barcode: ")
                            checkScannedBarcode(barcode[0]){
                                onResult(PayLoadModel(it, camera))
                            }
                            //onResult(PayLoadModel(barcode[0].displayValue, camera))

                            imageProxy.close()
                        } else imageProxy.close()
                    }.addOnFailureListener {

                        Log.d("TAGRD", "display value=> fail: ")
                        bitmapCropped.recycle()
                        imageProxy.close()
                    }
                }
            }
        }

        /*cameraModel.run {

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
                *//*val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()*//*

                imageAnalysis?.setAnalyzer(cameraExecutor) { imageProxy ->
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


                            bitmapCropped.recycle()
                            if (barcode.isNotEmpty()) {
                                checkScannedBarcode(barcode[0])
                                onResult(PayLoadModel(barcode[0].displayValue, camera))

                                Log.d("TAGb", "display value=> $barcode: ")
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
                        cameraSelector,
                        preview,
                        imageAnalysis
                    ).let {
                        camera = it
                        onResult(PayLoadModel("", camera))
                        Log.d("TAGGB", "camera obj 3: ${camera}")

                    }
                }
                catch (e: Exception) {
                    Log.e("CameraX", "Binding failed", e)
                }

            }, ContextCompat.getMainExecutor(context))
        }*/

    }


    fun captureAndScan(cameraModel: CameraModel, onResult: (PayLoadModel?) -> Unit) {
        cameraModel.run {

            CoroutineScope(Dispatchers.IO).launch {
                imageCapture?.takePicture(
                    ContextCompat.getMainExecutor(cameraModel.context),
                    object : ImageCapture.OnImageCapturedCallback() {

                        @OptIn(ExperimentalGetImage::class)
                        override fun onCaptureSuccess(imageProxy: ImageProxy) {
                            val mediaImage = imageProxy.image

                            Log.d("TAGRD", "onCaptureSuccess:  ${mediaImage}")
                            if (mediaImage != null) {
                                //val bitmap = ImageUtils.convertYuv420888ImageToBitmap(mediaImage) {}

                                //val inputImage = InputImage.fromBitmap(bitmap, 0)

                                val inputImage = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )


                                scanner.process(inputImage)
                                    .addOnSuccessListener { barcodes ->

                                        Log.d("TAGty", "onCaptureSuccess: ${barcodes.size}")

                                        if (barcodes.isNotEmpty()) {

                                            checkScannedBarcode(barcodes[0]){
                                                onResult(PayLoadModel(it, null))
                                            }

                                           /* onResult(
                                                PayLoadModel(
                                                    barcodes[0].displayValue,
                                                    null
                                                )
                                            )*/
                                        }
                                        //bitmap.recycle()
                                        imageProxy.close()
                                    }
                                    .addOnFailureListener {
                                        // bitmap.recycle()
                                        Log.d("TAGty", "addOnFailureListener:")
                                        imageProxy.close()
                                    }
                            } else {
                                imageProxy.close()
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.d("TAGRD", "onError  ${exception.message}")

                        }
                    }
                )
            }

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

    private fun checkScannedBarcode(code: Barcode, fromGallery: Boolean = false,onResult: (String?) -> Unit) {

        if ((code.displayValue?.isWebsite() == true || code.valueType == 5)) {
            val isBarcode = code.valueType == 5
            val url =
                if (code.valueType == 5) "https://www.google.com/search?q=${code.displayValue}"
                else code.displayValue
            logEvent("r=> ${isBarcode} ${url}")
            Log.d("SCANNER", "${isBarcode} ${url}")

            onResult(url)

        }
    }

}