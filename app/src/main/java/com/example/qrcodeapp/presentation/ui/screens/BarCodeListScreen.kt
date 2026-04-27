package com.example.qrcodeapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.qrcodeapp.data.dto.QrType
import com.example.qrcodeapp.domain.model.BarCodeModel
import com.example.qrcodeapp.domain.model.QrCode
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel

@Composable
fun BarCodeListScreen(qrEditorViewModel: QrEditorViewModel) {

    val barCoderList = qrEditorViewModel.qrCodeList.collectAsState().value
    val qrBarCodeData = qrEditorViewModel.qrBarCodeData.collectAsState().value
    val barCodeQRBitmap = qrEditorViewModel.barCodeBitmap.collectAsState().value

    LaunchedEffect(Unit) {
        qrEditorViewModel.fetchBarCodes()
    }
    LaunchedEffect(qrBarCodeData) {
        if (qrBarCodeData != null) {
            qrEditorViewModel.callBarCodeUseCase(BarCodeModel(qrBarCodeData, QrType.PDF417.name))
        }
    }
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .weight(.6f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = barCoderList,

                span = { item ->
                    if (item.name == QrType.DataMatrix.name) {
                        GridItemSpan(3)
                    } else {
                        GridItemSpan(1)
                    }
                }
            ) { qrCode ->
                BarcodeItemCard(qrCode) {
                    qrEditorViewModel.checkBarCode()
                }
            }

        }

        if (qrBarCodeData != null) {
            barCodeQRBitmap?.let {
                Image(
                    bitmap = barCodeQRBitmap.asImageBitmap(), "",
                    modifier = Modifier
                        .weight(.4f)
                )
            }
        }
    }

}


@Composable
fun BarcodeItemCard(qrCode: QrCode, onclick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable { onclick() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = qrCode.icon),
                contentDescription = qrCode.name,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = qrCode.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}
