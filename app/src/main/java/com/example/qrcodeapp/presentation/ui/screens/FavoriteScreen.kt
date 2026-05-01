package com.example.qrcodeapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.Constants
import com.example.qrcodeapp.core.utils.model.HistoryFavNavModel
import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.presentation.navigation.BarCodeResultScreen
import com.example.qrcodeapp.presentation.navigation.QRResultScreen
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel

@Composable
fun FavoriteScreen(navController: NavController, viewModel: QrEditorViewModel) {

    val tabs = listOf("QR Codes", "Barcodes")
    var selectedTab by remember { mutableIntStateOf(0) }
    val qrList = viewModel.qrListFav.collectAsState().value
    val barCodeList = viewModel.barcodeListFav.collectAsState().value

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
    {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {

            IconButton(onClick = {

            }) {
                Icon(
                    painter = painterResource(R.drawable.bak_arrow_icon),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }


            Spacer(modifier = Modifier.width(8.dp))


            Text(
                text = "Favourite",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily(Font(R.font.albertsans_medium)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 2.dp)
            )

        }

        // Top Bar Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Filter Chips
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, label ->
                    FilterChip(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        label = {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.onPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.onBackground,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedTab == index,
                            borderColor = MaterialTheme.colorScheme.tertiary,
                            selectedBorderColor = MaterialTheme.colorScheme.tertiary,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 0.dp
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.defaultMinSize(minWidth = 60.dp, minHeight = 40.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }


        }

        when (selectedTab) {
            0 -> {
                AllFavouriteScreen(qrList, viewModel,navController)
            }

            1 -> {
                AllFavouriteScreen(barCodeList, viewModel,navController)
            }


        }
    }
}


@Composable
fun AllFavouriteScreen(
    historyList: List<History>, viewModel: QrEditorViewModel,navController: NavController
) {
    if (historyList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {


            //items(historyList, key = { it.id }) { item ->

            itemsIndexed(historyList, key = { _, item -> item.id }) { index, item ->
                HistoryFavouriteItem(
                    item = item,
                    onFavClick = {
                        viewModel.updateFav(item.id)
                    },
                    onItemClick = {
                        Constants.is_from_history_or_fav = true
                        viewModel.setHisFavNav(
                            HistoryFavNavModel(
                                historyList[index].originalString,
                                historyList[index].resultedText,
                                historyList[index].dateString,
                                historyList[index].id
                            )
                        )
                        if (historyList[index].type == "BARCODE") {
                            navController.navigate(BarCodeResultScreen)
                        } else {
                            navController.navigate(QRResultScreen)
                        }
                    }
                )
            }
        }
    } else {
        EmptyHistoryCard(fontFamily = FontFamily(Font(R.font.visbycf_demibold)))
    }
}


@Composable
fun HistoryFavouriteItem(
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

            Image(
                painter = if (item.isFavourite) {
                    painterResource(R.drawable.fav_filled_icon)
                } else {
                    painterResource(R.drawable.fav_un_filled_icon)
                },
                contentDescription = "Favourite",
                modifier = Modifier
                    .size(22.dp)
                    .clickable {
                        onFavClick()
                    }
                ,

            )


        }
    }
}


