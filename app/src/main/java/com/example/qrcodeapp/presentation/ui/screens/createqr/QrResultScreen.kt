package com.example.qrcodeapp.presentation.ui.screens.createqr

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.AddHeight
import com.example.qrcodeapp.core.utils.AddWidth
import com.example.qrcodeapp.presentation.ui.theme.QrCodeAppTheme

@Composable
fun QrResultScreen() {
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
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.bak_arrow_icon),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Image(
                painter = painterResource(R.drawable.home_icon),
                contentDescription = "Home",
                modifier = Modifier.size(26.dp)
            )

        }

        // 2. Success Pill
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceBright)
                .clip(CircleShape)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.green_tick_icon),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "QR Code Generated Successfully!",
                    fontFamily = FontFamily(Font(R.font.albertsans_medium)),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. QR Display Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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
                        .size(240.dp)
                        .background(Color(0xFFF8F9FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("QR CODE", color = Color.LightGray)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(R.drawable.edit_icon),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )

            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. URL Field
        Box(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceBright)
                .padding(horizontal = 30.dp, vertical = 14.dp)
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "https://www.facebook.com/natgeo",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily(Font(R.font.albertsans_medium)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 2.dp)
                )
                AddWidth(8)
                Image(
                    painter = painterResource(R.drawable.copy_new_icon),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
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
            onClick = { },
            modifier = Modifier.offset(y= (-2).dp)
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
        Image(painter = painterResource(R.drawable.screen_bottom__line_bar_icon),"",
            modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        AddHeight(5)

    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun QrResultScreenPreviewDark() {
    QrCodeAppTheme(darkTheme = false) {
        QrResultScreen()
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


