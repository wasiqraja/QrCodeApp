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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.Constants.cam
import com.example.qrcodeapp.core.utils.Constants.isWebsite
import com.example.qrcodeapp.core.utils.ImageUtils
import com.example.qrcodeapp.core.utils.custom.CustomQRViewJava
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

    /* val context = LocalContext.current
     val lifecycleOwner = LocalLifecycleOwner.current
     var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }
     var viewFinderRef by remember { mutableStateOf<CustomQRViewJava?>(null) }
     val text = qrEditorViewModel.cameraText.collectAsState().value

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

     }*/


    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }
    var viewFinderRef by remember { mutableStateOf<CustomQRViewJava?>(null) }
    val text = qrEditorViewModel.cameraText.collectAsState().value

    var isFlashOn by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableFloatStateOf(0f) }


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


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { /* onBack() */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.bak_arrow_icon),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }



            Text(
                text = "Scan QR",
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                modifier = Modifier.padding(start = 10.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Flash button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (!isFlashOn) {
                            isFlashOn=true
                            cam?.cameraControl?.enableTorch(true)
                        }else{
                            isFlashOn=false
                            cam?.cameraControl?.enableTorch(false)
                        }

                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (isFlashOn) painterResource(R.drawable.flash_off_icon) else painterResource(
                        R.drawable.flash_icon
                    ),
                    contentDescription = "Flash",
                    tint = if (isFlashOn) Color.Yellow else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ── Hint Text ──────────────────────────────────────────────────
        Text(
            text = "Place a QR/Barcode inside the frame",
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.albertsans_medium)),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 280.dp)  // ← adjust to sit just below your QR frame
        )

        // ── Bottom Controls ────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /*// Zoom Slider
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Icon(
                    painter = painterResource(R.drawable.zoom_out_icon),
                    contentDescription = "Zoom Out",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
                Slider(
                    value = zoomLevel,
                    onValueChange = {
                        zoomLevel = it
                        // qrEditorViewModel.setZoom(it)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color(0xFF4268FF),
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.zoom_in_icon),
                    contentDescription = "Zoom In",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }*/



            ZoomSeekBar(
                zoomLevel = zoomLevel,
                onZoomChange = {
                    zoomLevel = it
                    // qrEditorViewModel.setZoom(it)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Gallery / Image picker
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { /* openGallery() */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.gallery_icon),
                        contentDescription = "Gallery",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.cam_capture_icon),
                    contentDescription = "Flip Camera",
                    tint = Color.White,
                    modifier = Modifier.size(72.dp)
                )

                /* // Capture / Shutter button
                 Box(
                     modifier = Modifier
                         .size(72.dp)
                         .clip(CircleShape)
                         .background(Color.White),
                     contentAlignment = Alignment.Center
                 )
                 {

                     Icon(
                         painter = painterResource(R.drawable.cam_capture_icon),
                         contentDescription = "Flip Camera",
                         tint = Color.White,
                         modifier = Modifier.size(72.dp)
                     )

                     Box(
                         modifier = Modifier
                             .size(60.dp)
                             .clip(CircleShape)
                             .background(Color.White)
                             .border(2.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
                             .clickable { *//* captureImage() *//* }
                    )
                }*/

                // Rotate / Flip camera
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { /* qrEditorViewModel.flipCamera() */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.turn_cam_icon),
                        contentDescription = "Flip Camera",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
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


@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoomSeekBar(
    zoomLevel: Float,
    onZoomChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color.Black.copy(alpha = 0.5f))  // ← dark pill background
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ── Left Icon ──────────────────────────────────────────────
        Icon(
            painter = painterResource(R.drawable.zoom_out_icon),       // replace later
            contentDescription = "Zoom Out",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )

        // ── Slider ─────────────────────────────────────────────────
        Slider(
            value = zoomLevel,
            onValueChange = onZoomChange,
            valueRange = 0f..1f,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent,               // ← no thumb visible
                activeTrackColor = Color(0xFF4268FF),         // ← blue track
                inactiveTrackColor = Color.White.copy(alpha = 0.3f),
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            thumb = {
                // invisible thumb — clean line look
                Spacer(modifier = Modifier.size(0.dp))
            }
        )

        // ── Right Icon ─────────────────────────────────────────────
        Icon(
            painter = painterResource(R.drawable.zoom_in_icon),       // replace later
            contentDescription = "Zoom In",
            tint = Color.White,
            modifier = Modifier.size(22.dp)              // slightly larger than left
        )
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