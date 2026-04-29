package com.example.qrcodeapp.presentation.ui.screens.createqr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.qrcodeapp.R
import com.example.qrcodeapp.data.local.entitiy.CreateQRCodeSocialModel
import com.example.qrcodeapp.data.mapper.QrCodeListProvider
import com.example.qrcodeapp.presentation.navigation.SocialDetailScreen
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel


@Composable
fun CreateQRScreen(navController: NavController,qrCodeListProvider: QrCodeListProvider,viewModel: QrEditorViewModel) {
    val fontFamilyVisby = FontFamily(Font(R.font.visbycf_demibold))
    val fontFamilyAlbert = FontFamily(Font(R.font.albertsans_semibold))

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        // Apply your screen padding here
        contentPadding = PaddingValues(start = 20.dp, end = 15.dp, top = 10.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // --- SECTION: TOP BAR ---
        item(span = { GridItemSpan(4) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CREATE",
                    fontFamily = fontFamilyAlbert,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "QR",
                    fontFamily = fontFamilyAlbert,
                    color = Color(0xFF4268FF),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(text = "CODE", fontFamily = fontFamilyAlbert, color = Color(0xFF4268FF))

                Spacer(Modifier.weight(1f))

                Image(painter = painterResource(R.drawable.crown_icon), contentDescription = "")
                Spacer(Modifier.width(10.dp))
                Icon(
                    painter = painterResource(R.drawable.history_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // --- SECTION: SOCIALS ---
        item(span = { GridItemSpan(4) }) {
            SectionHeader("Socials", fontFamilyVisby)
        }

        val socialList = qrCodeListProvider.getSocialList()
        items(socialList.size) { index ->
            SocialMediaGridItem(
                lightColor = socialList[index].iconColorLight,
                darkColor = socialList[index].iconColorDark,
                title = socialList[index].title,
                iconRes = socialList[index].resId,
                fontFamily = fontFamilyVisby,
                onClick = {
                    viewModel.setSocialModel(socialList[index])
                    navController.navigate(SocialDetailScreen)
                }
            )
        }

        // --- SECTION: GENERAL/UTILITY ---
        item(span = { GridItemSpan(4) }) {
            SectionHeader("General/Utility", fontFamilyVisby)
        }

        val generalList = qrCodeListProvider.getGeneralList()
        items(generalList.size) { index ->
            OtherMediaGridItem(
                title = generalList[index].title,
                iconRes = generalList[index].resId,
                fontFamily = fontFamilyVisby,
                onClick = { /* Handle Click */ }
            )
        }

        // --- SECTION: 2D BARCODE ---
        item(span = { GridItemSpan(4) }) {
            SectionHeader("2D Barcode", fontFamilyVisby)
        }

        val list2D = qrCodeListProvider.get2DBarCodeList()
        items(list2D.size) { index ->
            OtherMediaGridItem(
                title = list2D[index].title,
                iconRes = list2D[index].resId,
                fontFamily = fontFamilyVisby,
                onClick = { /* Handle Click */ }
            )
        }

        // --- SECTION: 1D BARCODE ---
        item(span = { GridItemSpan(4) }) {
            SectionHeader("1D Barcode (Linear Codes)", fontFamilyVisby)
        }

        val list1D = qrCodeListProvider.get1DBarCodeList()
        items(list1D.size) { index ->
            OtherMediaGridItem(
                title = list1D[index].title,
                iconRes = list1D[index].resId,
                fontFamily = fontFamilyVisby,
                onClick = { /* Handle Click */ }
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, fontFamily: FontFamily) {
    Column {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 15.dp)
        )
    }
}

@Composable
fun SocialMediaGridItem(
    lightColor: Color?,
    darkColor: Color?,
    title: String,
    iconRes: Int,
    fontFamily: FontFamily,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f) // Makes the card a perfect square
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (isSystemInDarkTheme()) {
                if (lightColor != null) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        modifier = Modifier.size(26.dp),
                        tint = darkColor!!
                    )
                } else {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        modifier = Modifier.size(26.dp)
                    )
                }
            } else {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontFamily = fontFamily,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun OtherMediaGridItem(
    title: String,
    iconRes: Int,
    fontFamily: FontFamily,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f) // Makes the card a perfect square
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(26.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontFamily = fontFamily,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
        CreateQRScreen(QrCodeListProvider())
    }
}*/
