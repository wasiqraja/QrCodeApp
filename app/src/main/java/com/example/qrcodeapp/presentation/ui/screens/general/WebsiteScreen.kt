package com.example.qrcodeapp.presentation.ui.screens.general

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.AddHeight


@Composable
fun WebsiteScreen() {

    var websiteUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
    )
    {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
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

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.website),
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.albertsans_medium)),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 2.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // ← controls shadow
                colors = CardDefaults.cardColors(containerColor = Color(0xFF337FFF))
            )
            {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.website_icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(38.dp))



            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceBright
                )
            )
            {
                Column(
                    modifier = Modifier.padding(20.dp)
                )
                {

                    Text(
                        text = stringResource(R.string.paste_your_website_link_here_to_create_a_qr_code),
                        fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    AddHeight(8)
                    // Custom-styled TextField
                    OutlinedTextField(
                        value = websiteUrl,
                        onValueChange = {
                            websiteUrl = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(
                                text = "Website URL",
                                fontSize = 12.sp,
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = .7f),
                                fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                        },
                        leadingIcon = {
                            // Placeholder for Link Icon
                            Image(
                                painter = painterResource(R.drawable.pin_icon),
                                "",
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        },
                        trailingIcon = {
                            Image(
                                painter = painterResource(R.drawable.clipboard_paste_icon),
                                "",
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                        ),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        if (websiteUrl.isNotEmpty()) {

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(bottom = 10.dp),
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
                        painter = painterResource(R.drawable.qr_pl_icon),
                        "",
                        modifier = Modifier
                            .size(23.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Generate QR Code",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        modifier = Modifier.padding(start = 1.dp, end = 1.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.star_icon),
                        "",
                        modifier = Modifier
                            .size(13.dp)
                            .offset(y = (-8).dp)
                    )
                }
            }
        } else {

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                        painter = painterResource(R.drawable.qr_pl_icon),
                        "",
                        modifier = Modifier
                            .size(23.dp),
                        tint = Color(0xFF6E6E6E).copy(alpha = .7f)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Generate QR Code",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(0xFF6E6E6E).copy(alpha = .7f),
                        modifier = Modifier.padding(start = 1.dp, end = 1.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.star_icon),
                        "",
                        modifier = Modifier
                            .size(13.dp)
                            .offset(y = (-8).dp),
                        tint = Color(0xFFFFFFFF)
                    )
                }
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