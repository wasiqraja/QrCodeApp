package com.example.qrcodeapp.presentation.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.Constants
import com.example.qrcodeapp.core.utils.custom.CustomQRViewJava
import com.example.qrcodeapp.core.utils.imageAnalysis
import com.example.qrcodeapp.core.utils.imageCapture
import com.example.qrcodeapp.domain.model.CameraModel
import com.example.qrcodeapp.presentation.navigation.BarCodeResultScreen
import com.example.qrcodeapp.presentation.navigation.QRResultScreen
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning

@Composable
fun CameraScreen(
    qrEditorViewModel: QrEditorViewModel,navController: NavController
) {

    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    val scanner: BarcodeScanner by lazy { BarcodeScanning.getClient() }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }
    var viewFinderRef by remember { mutableStateOf<CustomQRViewJava?>(null) }
    val text = qrEditorViewModel.cameraText.collectAsState().value
    val cameraObj = qrEditorViewModel.camera.collectAsState().value


    var isFlashOn by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableFloatStateOf(0f) }


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

            qrEditorViewModel.initCamera(
                CameraModel(
                    context,
                    lifecycleOwner,
                    previewViewRef!!,
                    viewFinderRef!!,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    imageCapture = imageCapture,
                    imageAnalysis = imageAnalysis
                )
            )

            qrEditorViewModel.startCameraUseCase(
                CameraModel(
                    context,
                    lifecycleOwner,
                    previewViewRef!!,
                    viewFinderRef!!,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    imageCapture = imageCapture,
                    imageAnalysis = imageAnalysis
                )
            )


        }
    }

    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->

        uri?.let {
            qrEditorViewModel.getTextFromImage(scanner, uri, context){

                Log.d("VTAG", "CameraScreen: ${it}")
                qrEditorViewModel.setText(it)
                if (Constants.isFrom=="qr"){
                    navController.navigate(QRResultScreen)
                }else {
                    navController.navigate(BarCodeResultScreen)
                }
            }
        }
    }

    LaunchedEffect(text) {
        if (text!=null){
            if (Constants.isFrom=="qr"){
                navController.navigate(QRResultScreen)
            }else {
                navController.navigate(BarCodeResultScreen)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {


        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    previewViewRef = this
                }
            }, modifier = Modifier.fillMaxSize()
        )


        AndroidView(
            factory = { ctx ->
                CustomQRViewJava(ctx).apply {
                    viewFinderRef = this
                }
            }, modifier = Modifier.fillMaxSize()
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        )
        {
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
                            isFlashOn = true
                            cameraObj?.cameraControl?.enableTorch(true)
                        } else {
                            isFlashOn = false
                            cameraObj?.cameraControl?.enableTorch(false)
                        }

                    }, contentAlignment = Alignment.Center
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
                .align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally
        )
        {


            ZoomSeekBar(
                zoomLevel = zoomLevel, onZoomChange = {
                    zoomLevel = it
                    // qrEditorViewModel.setZoom(it)
                }, modifier = Modifier.fillMaxWidth()
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
                        .clickable {

                            pickImagesLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )

                        }, contentAlignment = Alignment.Center
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
                    modifier = Modifier
                        .size(72.dp)
                        .clickable {
                            qrEditorViewModel.takeImage(
                                CameraModel(
                                    context,
                                    lifecycleOwner,
                                    previewViewRef!!,
                                    viewFinderRef!!,
                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                    imageCapture = imageCapture,
                                    imageAnalysis = imageAnalysis

                                )
                            )
                        }
                )

                // Rotate / Flip camera
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable {

                            if (isFlashOn) {
                                isFlashOn = false
                                cameraObj?.cameraControl?.enableTorch(false)
                            }

                            cameraSelector =
                                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                                else CameraSelector.DEFAULT_BACK_CAMERA

                            qrEditorViewModel.initCamera(
                                CameraModel(
                                    context,
                                    lifecycleOwner,
                                    previewViewRef!!,
                                    viewFinderRef!!,
                                    cameraSelector,
                                    imageCapture = imageCapture,
                                    imageAnalysis = imageAnalysis
                                )
                            )
                        },
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


}


@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoomSeekBar(
    zoomLevel: Float, onZoomChange: (Float) -> Unit, modifier: Modifier = Modifier
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
            contentDescription = "Zoom Out", tint = Color.White, modifier = Modifier.size(18.dp)
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
            })

        // ── Right Icon ─────────────────────────────────────────────
        Icon(
            painter = painterResource(R.drawable.zoom_in_icon),       // replace later
            contentDescription = "Zoom In",
            tint = Color.White,
            modifier = Modifier.size(22.dp)              // slightly larger than left
        )
    }
}

