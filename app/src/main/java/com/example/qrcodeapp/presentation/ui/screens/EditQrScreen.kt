package com.example.qrcodeapp.presentation.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.model.QrTemplate
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel


@Composable
fun EditQrCodeScreen(viewModel: QrEditorViewModel) {

    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val bitmapNew by viewModel.qrBitmap.collectAsState()
    val isPictureApplied = remember { mutableStateOf(false) }
    val optionOne=remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.initQrDrawable {
            bitmap.value = it
        }
    }


    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(modifier = Modifier.padding(top = 10.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                optionOne.value=true
            }) {
                Text(text = "Style Hor")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                optionOne.value=false
            }) {
                Text(text = "Style Ver")
            }
        }

        bitmap.value?.asImageBitmap()?.let {
            Image(
                bitmap = it,
                contentDescription = "Scanned Image",
                modifier = Modifier.size(150.dp),
            )
        }

        bitmapNew?.let {


            val graphicsLayer = rememberGraphicsLayer()

            Box(
                modifier = Modifier.drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            }
            )
            {
                if (optionOne.value) {
                    QrTemplateView(it.asImageBitmap(), QrTemplate(), isPictureApplied)
                }else

                    /*QrTemplateViewDesignVerticleImage(it.asImageBitmap(), QrTemplate(
                        scannerRes = R.drawable.pur_top,R.drawable.per_bottom, scannerToBubbleRatio = .75f

                    ), isPictureApplied)*/

                QrTemplateViewDesignVerticleImageBlue(it.asImageBitmap(), QrTemplate(
                    scannerRes = R.drawable.blue_bot,R.drawable.blue_top, scannerToBubbleRatio = .65f, qrPaddingScale = 0.09f

                ), isPictureApplied)


                /*QrTemplateViewDesign2(it.asImageBitmap(), QrTemplate(
                    bubbleRes = R.drawable.temp_below,
                    scannerToBubbleRatio = .50f,
                    qrPaddingScale = .09f
                ), isPictureApplied)*/

            }

        }


        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            isPictureApplied.value = false
            //viewModel.applyTemplate(5, -1,ForegroundColor("#FF0000"), BackgroundColor(solidColor = "#16ADA8"))
            viewModel.useCase1()
        }) {
            Text("Apply Color")
        }
        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            isPictureApplied.value = false
            viewModel.applyGradientUseCase()
            //viewModel.applyTemplate(5,-1, ForegroundColor(null,Gradient(startColor = "#37DB47", endColor = "#E4DFAD",R.drawable.qr_background_3,null)), BackgroundColor(solidColor = "#16ADA8"))
        }) {
            Text("Apply Gradient")
        }

        Button(onClick = {
            isPictureApplied.value = true
            viewModel.applyPictureUseCase()
            /*viewModel.applyTemplate(5,-1,
                ForegroundColor(null,Gradient(startColor = "#FF0000", endColor = "#FF0000",R.drawable.qr_background_3,null)),
                BackgroundColor(imageDrawable = R.drawable.qr_background_3))*/
        }) {
            Text("Apply Picture")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            isPictureApplied.value = false
            viewModel.applyLogoUseCase()
            /*viewModel.applyTemplate(5,
                R.drawable.ic_youtube,
                ForegroundColor(null,Gradient(startColor = "#FF0000", endColor = "#FF0000",R.drawable.qr_background_3,null)),
                BackgroundColor(imageDrawable = R.drawable.qr_background_3))*/

        }) {
            Text("Apply Logo")
        }


    }

}

@Composable
fun QrTemplateViewb(
    qrBitmap: ImageBitmap?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 70.dp)
            .width(300.dp)
            .height(130.dp)
            //.clip(RoundedCornerShape(20.dp))
            .paint(
                painter = painterResource(id = R.drawable.template5),
                contentScale = ContentScale.FillBounds
            )
    ) {
        qrBitmap?.let {

            Image(
                bitmap = it,
                contentDescription = "Custom QR",
                modifier = Modifier
                    .width(110.dp)
                    .height(140.dp)
                    //.background(color = Color.Red)
                    .align(Alignment.CenterStart)
                    .padding(start = 7.dp)

            )
        }
    }
}

@Composable
fun QrTemplateViewp(
    qrBitmap: ImageBitmap?,
    modifier: Modifier = Modifier
) {
    // Parent Container (Total Canvas Area)
    Box(
        modifier = modifier
            .wrapContentWidth()
            //.width(320.dp) // Adjusted slightly for the bubble tail
            .height(150.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        // 1. The Scanner Corners (image_3cda99)
        Image(
            painter = painterResource(id = R.drawable.sca),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp) // Base size for the scanner area
                .padding(start = 10.dp)
                .alpha(0f),
            contentScale = ContentScale.Fit
        )

        // 2. The "Scan Me!" Bubble (image_3cdadc)
        // Positioned to the right of the scanner box
        Image(
            painter = painterResource(id = R.drawable.bubble),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 125.dp) // Offset to overlap the tail with the box
                .width(180.dp)
                .height(90.dp)
                .align(Alignment.CenterStart),
            contentScale = ContentScale.FillBounds
        )

        // 3. The QR Code (Placed precisely inside the corners)
        qrBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = "Generated QR",
                modifier = Modifier
                    .padding(start = 19.dp) // Centers it inside the corners
                    .size(106.dp) // Slightly smaller than the corners
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White) // Essential for scannability
            )
        }
    }
}


@Composable
fun QrTemplateViewa(
    qrBitmap: ImageBitmap?,
    template: QrTemplate,
    modifier: Modifier = Modifier
) {
    // We use a Row to hold the two pieces of the template
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), // Height adjusts to match the content
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- 1. THE SCANNER SECTION ---
        Box(
            modifier = Modifier
                .weight(template.scannerToBubbleRatio) // Dynamic Width
                .aspectRatio(1f), // Keep it a perfect square
            contentAlignment = Alignment.Center
        ) {
            // Scanner Corners
            Image(
                painter = painterResource(id = template.scannerRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
            )

            // The QR Code - Scaled dynamically within the scanner
            qrBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "QR",
                    modifier = Modifier
                        .fillMaxSize(1f - (template.qrPaddingScale * 2))
                        .background(Color.White) // Contrast for scanning
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        // --- 2. THE BUBBLE SECTION ---
        Box(
            modifier = Modifier
                .weight(1f - template.scannerToBubbleRatio) // Takes remaining space
                .offset(x = (-8).dp)
            // Slight negative offset to "tuck" the tail under the scanner
        ) {
            Image(
                painter = painterResource(id = template.bubbleRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Gray),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
fun QrTemplateView(
    qrBitmap: ImageBitmap?,
    template: QrTemplate,
    isPicApplied: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .wrapContentWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(template.scannerToBubbleRatio)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = template.scannerRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(
                        if (isPicApplied.value) {
                            0f
                        } else {
                            1f
                        }
                    )
            )

            qrBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "QR",
                    modifier = Modifier
                        .fillMaxSize(1f - (template.qrPaddingScale * 2))
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Image(
            painter = painterResource(id = template.bubbleRes),
            contentDescription = null,
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(0.7f)
                .offset(x = (-12).dp),
            contentScale = ContentScale.FillHeight
        )
    }
}

@Composable
fun QrTemplateViewDesign2(
    qrBitmap: ImageBitmap?,
    template: QrTemplate,
    isPicApplied: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .wrapContentWidth()
            .height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(template.scannerToBubbleRatio)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {

            qrBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "QR",
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Image(
            painter = painterResource(id = template.bubbleRes),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .background(color = Color.Gray)
            ,
            contentScale = ContentScale.FillHeight
        )
    }
}


@Composable
fun QrTemplateViewDesignVerticleImage(
    qrBitmap: ImageBitmap?,
    template: QrTemplate,
    isPicApplied: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .wrapContentWidth()
            .height(IntrinsicSize.Min),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(template.scannerToBubbleRatio)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = template.scannerRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(
                        if (isPicApplied.value) {
                            0f
                        } else {
                            1f
                        }
                    )
            )

            qrBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "QR",
                    modifier = Modifier.offset(y= (-6).dp)
                        .fillMaxSize(1f - (template.qrPaddingScale * 2))
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Image(
            painter = painterResource(id = template.bubbleRes),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
            ,
            contentScale = ContentScale.FillHeight
        )
    }
}


@Composable
fun QrTemplateViewDesignVerticleImageBlue(
    qrBitmap: ImageBitmap?,
    template: QrTemplate,
    isPicApplied: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .wrapContentWidth()
            .height(IntrinsicSize.Min),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = template.bubbleRes),
            contentDescription = null,
            modifier = Modifier
                //.wrapContentSize()
                .fillMaxWidth(template.scannerToBubbleRatio)
                .wrapContentHeight()
            ,
            contentScale = ContentScale.FillHeight
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(template.scannerToBubbleRatio)
                .aspectRatio(1f).offset(y = (-15).dp),
            contentAlignment = Alignment.Center
        )
        {

            Image(
                painter = painterResource(id = template.scannerRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(
                        if (isPicApplied.value) {
                            0f
                        } else {
                            1f
                        }
                    )
            )

            qrBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "QR",
                    modifier = Modifier
                        .fillMaxSize(1f - (template.qrPaddingScale * 2))
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }


    }
}


