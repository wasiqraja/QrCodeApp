package com.example.qrcodeapp.presentation.ui.screens.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.AddHeight
import com.example.qrcodeapp.core.utils.Constants
import com.example.qrcodeapp.core.utils.Constants.saveBitmapToDownloads
import com.example.qrcodeapp.core.utils.copyToClipboard
import com.example.qrcodeapp.core.utils.getFormattedDateTime
import com.example.qrcodeapp.core.utils.shareQR
import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraQrResultScreen(viewModel: QrEditorViewModel, navController: NavController) {

    val activity = LocalActivity.current
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val text = viewModel.cameraText.collectAsState().value

    val isFav = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val hisFavNavModel = viewModel.historyFavNavModel.collectAsState()

    // Permission launcher (for Android 9 and below)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            saveBitmapToDownloads(
                context,
                bitmap.value!!,
                "image_${System.currentTimeMillis()}.png"
            )
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkAndSaveBitmap() {
        bitmap.value?.let {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    saveBitmapToDownloads(
                        context,
                        bitmap.value!!,
                        "image_${System.currentTimeMillis()}.png"
                    )
                } else {
                    permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            } else {
                // No permission needed for Android 10+
                saveBitmapToDownloads(
                    context,
                    bitmap.value!!,
                    "image_${System.currentTimeMillis()}.png"
                )
            }
        }
    }

    BackHandler {
        Constants.is_from_history_or_fav = false
        viewModel.resetHisFavNav()
        viewModel.resetText()
        navController.navigateUp()
    }

    LaunchedEffect(Unit) {
        if (!Constants.is_from_history_or_fav) {
            text?.let {
                viewModel.initQrDrawable(text) {
                    bitmap.value = it
                    viewModel.save(History(0, "abc", text!!, "QR", getFormattedDateTime(), false))

                }
            }
        } else {

            hisFavNavModel.value?.resultText?.let {
                viewModel.initQrDrawable(hisFavNavModel.value!!.resultText!!) {
                    bitmap.value = it
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 15.dp)
    ) {
        // 1. Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            IconButton(onClick = {
                viewModel.resetText()
                navController.navigateUp()
            }) {
                Icon(
                    painter = painterResource(R.drawable.bak_arrow_icon),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (!Constants.is_from_history_or_fav) {
                Image(
                    painter = painterResource(R.drawable.del_icon),
                    contentDescription = "Home",
                    modifier = Modifier
                        .size(26.dp)
                        .clickable {
                            isFav.value = false
                            viewModel.delete()
                            /* if (!Constants.is_from_history_or_fav) {
                                 viewModel.delete()
                             } else {
                                 hisFavNavModel.value?.id?.let { id ->
                                     viewModel.deleteHisFav(id)
                                 }
                             }*/
                            viewModel.resetText()
                            navController.navigateUp()
                        }
                )
            }

        }



        Spacer(modifier = Modifier.height(20.dp))

        // 3. QR Display Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        )
        {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                // QR Placeholder

                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 20.dp, vertical = 7.dp)
                )
                {
                    Text(
                        text = "Scanned",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                    )


                }

                AddHeight(10)

                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color(0xFFF8F9FA)),
                    contentAlignment = Alignment.Center
                ) {
                    bitmap.value?.asImageBitmap()?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "Scanned Image",
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (!Constants.is_from_history_or_fav) {
                    text?.let {
                        Text(
                            text = text,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                        )
                    }
                } else {

                    hisFavNavModel.value?.resultText?.let {
                        it
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                        )
                    }

                }


                Text(
                    text = if (!Constants.is_from_history_or_fav) {
                        getFormattedDateTime()
                    } else {
                        hisFavNavModel.value?.dateTime.toString()
                    },
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                )


            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. URL Field

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clickable {
                        if (!Constants.is_from_history_or_fav) {
                            viewModel.favourite(true, favStatus = {
                                isFav.value = it
                            })
                        } else {
                            hisFavNavModel.value?.id?.let {
                                viewModel.favouriteFromHisFav(
                                    true,
                                    hisFavNavModel.value?.id!!,
                                    favStatus = {
                                        isFav.value = it
                                    })
                            }
                        }
                    }
                    .padding(horizontal = 25.dp)
                    .weight(1f)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceBright)
                    .padding(horizontal = 30.dp, vertical = 20.dp)
            )
            {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(
                            if (isFav.value) {
                                R.drawable.fav_filled_icon
                            } else {
                                R.drawable.fav_un_filled_icon
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    AddHeight(3)
                    Text(
                        text = "Favorite",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.albertsans_medium)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                    )


                }
            }

            Box(
                modifier = Modifier
                    .clickable {
                        if (!Constants.is_from_history_or_fav) {
                            text?.let {
                                activity?.copyToClipboard(text)
                            }
                        } else {
                            hisFavNavModel.value?.resultText?.let { it ->
                                activity?.copyToClipboard(it)
                            }
                        }
                    }
                    .padding(horizontal = 25.dp)
                    .weight(1f)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceBright)
                    .padding(horizontal = 30.dp, vertical = 20.dp)

            )
            {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.copy_icon_qr),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    AddHeight(3)
                    Text(
                        text = "Copy Link",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.albertsans_medium)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 2.dp, end = 2.dp)
                    )


                }
            }
        }


        AddHeight(15)

        Button(
            onClick = {
                checkAndSaveBitmap()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Placeholder for QR Icon
                Image(
                    painter = painterResource(R.drawable.save_image_icon),
                    "",
                    modifier = Modifier
                        .size(23.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Save Image",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier.padding(start = 1.dp, end = 1.dp)
                )

            }
        }



        Button(
            onClick = {
                bitmap.value?.let {
                    activity?.shareQR(bitmap.value!!)
                }
            },
            modifier = Modifier
                .offset(y = (-2).dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(56.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
        {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Placeholder for QR Icon
                Icon(
                    painter = painterResource(R.drawable.share_image_icon),
                    "",
                    modifier = Modifier
                        .size(23.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Share Image",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 1.dp, end = 1.dp)
                )

            }
        }

        AddHeight(30)
        Image(
            painter = painterResource(R.drawable.screen_bottom__line_bar_icon), "",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        AddHeight(5)

    }


}


/*
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QrResultScreenPreview() {
    QrCodeAppTheme(darkTheme = true) {
        QrResultScreen()
    }
}
*/


