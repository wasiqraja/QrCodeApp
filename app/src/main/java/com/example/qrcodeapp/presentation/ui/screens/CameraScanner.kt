package com.example.qrcodeapp.presentation.ui.screens

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
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.qrcodeapp.core.utils.Constants.isWebsite
import com.example.qrcodeapp.core.utils.ImageUtils
import com.example.qrcodeapp.core.utils.custom.CustomQRViewJava
import com.example.qrcodeapp.core.utils.logEvent
import com.example.qrcodeapp.domain.model.CameraModel
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import kotlin.math.abs

@Composable
fun CameraScreen(
    qrEditorViewModel: QrEditorViewModel,
    onScanResult: (String) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }
    var viewFinderRef by remember { mutableStateOf<CustomQRViewJava?>(null) }
    var text = qrEditorViewModel.cameraText.collectAsState().value

    LaunchedEffect(text) {
        logEvent("camera : ${text.toString()}")
    }


    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    previewViewRef = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        AndroidView(
            factory = { ctx ->
                CustomQRViewJava(ctx).apply {
                    viewFinderRef = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

    }

    LaunchedEffect(previewViewRef, viewFinderRef) {
        if (previewViewRef != null && viewFinderRef != null) {
            /*startCameraXCompose(
                context = context,
                lifecycleOwner = lifecycleOwner,
                previewView = previewViewRef!!,
                viewFinder = viewFinderRef!!,
                onScanResult = onScanResult
            )
            */
            qrEditorViewModel.startCameraUseCase(
                CameraModel(
                    context,
                    lifecycleOwner,
                    previewViewRef!!,
                    viewFinderRef!!
                )
            )
        }
    }
}

@OptIn(ExperimentalGetImage::class)
private fun startCameraXCompose(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    viewFinder: CustomQRViewJava,
    onScanResult: (String) -> Unit
) {
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

        Log.d("SCANNER", "${isBarcode} ${url}")

    }
}