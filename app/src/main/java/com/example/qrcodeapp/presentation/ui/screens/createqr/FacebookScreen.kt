package com.example.qrcodeapp.presentation.ui.screens.createqr

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.AddHeight
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacebookScreen(viewModel: QrEditorViewModel) {

    var facebookUrl by remember { mutableStateOf("") }
    val model = viewModel.socialModel.collectAsState().value

    LaunchedEffect(Unit) {
        Log.d("TAGhd", "FacebookScreen: ${viewModel.socialModel}")
    }


    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 1. Top Bar
        TopAppBar(
            title = {

                Text(
                    text = model?.title ?: "",
                    fontFamily = FontFamily(Font(R.font.albertsans_medium)),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 2.dp)
                )
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.bak_arrow_icon),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // 2. Main Content (Scrollable if needed, using weight to push button down)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))



            if (model?.detailIcon!=null) {
                Image(
                    painter = painterResource(model.detailIcon),
                    contentDescription = null,
                    modifier = Modifier.offset(y = (-45).dp)
                        .size(180.dp),
                    contentScale = ContentScale.FillBounds
                )
            }



            // White Card for Input
            Card(
                modifier = Modifier.fillMaxWidth().offset(y = (-45).dp),
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
                        text = model?.heading ?: "",
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
                        value = facebookUrl,
                        onValueChange = { facebookUrl = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(
                                text = model?.subTitle ?: "",
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
        }

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
    }
}





