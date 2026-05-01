package com.example.qrcodeapp.presentation.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.AddHeight
import com.example.qrcodeapp.core.utils.AddWidth
import com.example.qrcodeapp.core.utils.Constants
import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.presentation.navigation.CameraScreen
import com.example.qrcodeapp.presentation.navigation.FavouriteScreen
import com.example.qrcodeapp.presentation.navigation.HistoryScreen
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel


@Composable
fun HomeScreen(navController: NavController, viewModel: QrEditorViewModel) {


    val context = LocalContext.current
    val historyList = viewModel.historyList.collectAsState().value

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            navController.navigate(CameraScreen)
        } else {
            Toast.makeText(context, "Camera permission required", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchHistory()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(start = 20.dp, end = 15.dp, top = 10.dp)

    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.qr_code),
                fontFamily = FontFamily(Font(R.font.albertsans_semibold)),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 2.dp)
            )

            Text(
                text = stringResource(R.string.scanner),
                fontFamily = FontFamily(Font(R.font.albertsans_semibold)),
                color = Color(0xFF4268FF),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 2.dp, end = 2.dp)
            )

            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.crown_icon),
                ""
            )
            AddWidth(10)
            Icon(
                painter = painterResource(R.drawable.history_icon),
                "",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        AddHeight(30)

        Text(
            text = "Explore  Features",
            fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(end = 2.dp)
        )

        AddHeight(15)

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            ScanOptionCard(
                modifier = Modifier.weight(1f),
                title = "Scan QR Code",
                subtitle = "scan",
                iconRes = R.drawable.scan_qr_code_home_icon, // Replace with your drawable
                backgroundColor = Color(0xFFFF6D00),
                onClick = {
                    Constants.isFrom = "qr"
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED -> {

                            navController.navigate(CameraScreen)
                        }

                        else -> {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            )

            ScanOptionCard(
                modifier = Modifier.weight(1f),
                title = "Scan Bar Code",
                subtitle = "scan",
                iconRes = R.drawable.scan_bar_code_home_icon, // Replace with your drawable
                backgroundColor = Color(0xFFFF6D00), // Orange
                onClick = {
                    Constants.isFrom = "bar"
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED -> {

                            navController.navigate(CameraScreen)
                        }

                        else -> {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            )
        }
        AddHeight(8)
        CreateQRMenuItem(
            modifier = Modifier.fillMaxWidth(),
            title = "Create QR Code",
            subtitle = "Add",
            iconRes = R.drawable.create_qr_code_home_icon,
            onClick = { /* Navigate to Create QR screen */ }
        )
        AddHeight(15)
        Text(
            text = "Recent Scans",
            fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(end = 2.dp)
        )

        AddHeight(10)

        if (historyList.isEmpty()) {
            EmptyRecentScansCard(
                fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                onStartScanningClick = {

                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-14).dp),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(R.drawable.zig_zag_home_icon), "")
            }
        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                items(historyList, key = { it.id }) { item ->
                    HistoryItemHome(
                        item = item,
                        onFavClick = {
                            viewModel.updateFav(item.id)
                        },
                        onItemClick = {
                            //navController.navigate(HistoryScreen)
                            navController.navigate(FavouriteScreen)
                        }
                    )
                }
            }
        }


    }
}


@Composable
fun HistoryItemHome(
    item: History,
    onFavClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(interactionSource = null, indication = null) {
                onItemClick()
            }
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // QR Icon Box
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceBright,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (item.type == "QR") painterResource(R.drawable.qr_icon) else painterResource(
                        R.drawable.bar_code_icon
                    ),
                    contentDescription = item.type,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // URL + Date
            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = item.resultedText,
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 2.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.dateString,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 2.dp)
                )
            }


            // Fav Button
            Icon(
                painter = if (item.isFavourite) {
                    painterResource(R.drawable.fav_filled_icon)
                } else {
                    painterResource(R.drawable.fav_un_filled_icon)
                },
                contentDescription = "Favourite",
                modifier = Modifier
                    .size(22.dp)
                    .clickable { onFavClick() },
                tint = Color.Unspecified // 👈 IMPORTANT
            )


        }
    }
}


@Composable
fun ScanOptionCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier // Use the passed-in modifier here
            .fillMaxHeight(.25f)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Section: Icon and Colored Background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.6f)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                // Main Icon
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()

                )
            }

            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceBright)
                    .padding(
                        horizontal = 10.dp,
                        vertical = 10.dp
                    )
                    .fillMaxWidth()
                    .weight(0.4f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier) {

                    Text(
                        text = subtitle,
                        fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 2.dp)
                    )

                    Text(
                        text = title,
                        fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 2.dp)
                    )

                }

                Icon(
                    painter = painterResource(R.drawable.card_arrow_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun CreateQRMenuItem(
    modifier: Modifier = Modifier, // Keep this modifier to allow external styling (like weight/padding)
    title: String,
    subtitle: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright)
    )
    {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically // Vertically center content in the Row
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.28f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes), // The pattern texture
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceBright)
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Text Column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subtitle,
                        fontFamily =
                            FontFamily(Font(R.font.visbycf_demibold)),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 2.dp)
                    )

                    Text(
                        text = title,
                        fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.card_arrow_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


@Composable
fun EmptyRecentScansCard(
    modifier: Modifier = Modifier,
    fontFamily: FontFamily,
    onStartScanningClick: () -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        ),
    )

    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            // Folder Icon
            Icon(
                painter = painterResource(id = R.drawable.home_folder_icon), // Replace with your icon
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFFB0B0B0)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Main Title
            Text(
                text = "No Recent Scans",
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sub-description
            Text(
                text = "Your scanned QR and barcodes will appear here",
                fontFamily = fontFamily,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Start Scanning Button/Link
            Text(
                text = "Start Scanning",
                fontFamily = fontFamily,
                fontSize = 13.sp,
                modifier = Modifier
                    .clickable { onStartScanningClick() }
                    .padding(8.dp),

                color = Color(0xFF4268FF),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
/*

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}
*/

