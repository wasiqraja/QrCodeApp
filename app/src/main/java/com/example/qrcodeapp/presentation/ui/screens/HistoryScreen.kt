package com.example.qrcodeapp.presentation.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.qrcodeapp.core.utils.AddHeight
import com.example.qrcodeapp.core.utils.AddWidth
import com.example.qrcodeapp.core.utils.Constants
import com.example.qrcodeapp.core.utils.SortOrder
import com.example.qrcodeapp.core.utils.model.HistoryFavNavModel
import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.presentation.navigation.BarCodeResultScreen
import com.example.qrcodeapp.presentation.navigation.QRResultScreen
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(navController: NavController, viewModel: QrEditorViewModel) {

    val tabs = listOf("All", "QR Codes", "Barcodes")
    var selectedTab by remember { mutableStateOf(0) }
    val historyList = viewModel.historyList.collectAsState().value
    val qrList = viewModel.qrList.collectAsState().value
    val barCodeList = viewModel.barcodeList.collectAsState().value

    var showDeleteSheet by remember { mutableStateOf(false) }
    var isSelectedTextVisible by remember { mutableStateOf(false) }
    var isCrossIconVisible by remember { mutableStateOf(false) }
    var isBlueTickVisible by remember { mutableStateOf(false) }
    var isDeleteVisible by remember { mutableStateOf(false) }
    var showSortSheet by remember { mutableStateOf(false) }

    val selectedItems = remember { mutableStateListOf<Int>() }
    val selectedItemsQrCode = remember { mutableStateListOf<Int>() }
    val selectedItemsBarCode = remember { mutableStateListOf<Int>() }

    val isAllSelected = selectedItems.size == historyList.size
    val isAllSelectedQr = selectedItemsQrCode.size == qrList.size
    val isAllSelectedBar = selectedItemsBarCode.size == barCodeList.size


    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
    {

        if (showSortSheet) {
            SortBottomSheet(
                onDismiss = { showSortSheet = false },
                onApply = { sortBy, order, orderDate ->
                    showSortSheet = false
                    Log.d("TAGCV", "SORT BY: ${sortBy}")
                    Log.d("TAGCV", "Order: ${order}")
                    //viewModel.sortList()
                    if (sortBy == "Name") {
                        when (order) {
                            "AtoZ" -> {
                                viewModel.sortList(SortOrder.ASC)
                            }

                            "ZtoA" -> {
                                viewModel.sortList(SortOrder.DESC)
                            }
                        }
                    } else {
                        when (orderDate) {
                            "Latest" -> {
                                viewModel.sortListViaDateAsc()
                            }

                            "Oldest" -> {
                                viewModel.sortListViaDateDesc()
                            }
                        }

                    }
                }
            )
        }


        if (showDeleteSheet) {
            DeleteConfirmationBottomSheet(
                onDismiss = { showDeleteSheet = false },
                onConfirmDelete = {
                    showDeleteSheet = false
                    //viewModel.deleteById(item.id)
                    when (selectedTab) {
                        0 -> {
                            if (selectedItems.isEmpty()) return@DeleteConfirmationBottomSheet
                            viewModel.deleteSelected(selectedItems.toList())
                        }

                        1 -> {
                            if (selectedItemsQrCode.isEmpty()) return@DeleteConfirmationBottomSheet
                            viewModel.deleteSelected(selectedItemsQrCode.toList())
                        }

                        2 -> {
                            if (selectedItemsBarCode.isEmpty()) return@DeleteConfirmationBottomSheet
                            viewModel.deleteSelected(selectedItemsBarCode.toList())
                        }
                    }

                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            if (!isCrossIconVisible) {
                IconButton(onClick = {

                }) {
                    Icon(
                        painter = painterResource(R.drawable.bak_arrow_icon),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.cross_clip_icon),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(interactionSource = null, indication = null) {
                            isDeleteVisible = false
                            isBlueTickVisible = false
                            isCrossIconVisible = false
                            isSelectedTextVisible = false
                            selectedItems.clear()
                            selectedItemsQrCode.clear()
                            selectedItemsBarCode.clear()

                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (!isSelectedTextVisible) {
                Text(
                    text = "History",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily(Font(R.font.albertsans_medium)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 2.dp)
                )
            } else {

                AnimatedVisibility(visible = true) {
                    Text(
                        text = when (selectedTab) {
                            0 -> {
                                "${selectedItems.size} Selected"
                            }

                            1 -> {
                                "${selectedItemsQrCode.size} Selected"
                            }

                            2 -> {
                                "${selectedItemsBarCode.size} Selected"
                            }

                            else -> {
                                ""
                            }
                        },
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.albertsans_medium)),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }

            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(visible = true) {
                Image(
                    painter = painterResource(
                        when (selectedTab) {
                            0 -> {
                                if (isAllSelected && isDeleteVisible) R.drawable.tick_blue_filled_icon else R.drawable.tick_gray_un_filled_icon
                            }

                            1 -> {
                                if (isAllSelectedQr && isDeleteVisible) R.drawable.tick_blue_filled_icon else R.drawable.tick_gray_un_filled_icon
                            }

                            else -> {
                                if (isAllSelectedBar && isDeleteVisible) R.drawable.tick_blue_filled_icon else R.drawable.tick_gray_un_filled_icon
                            }

                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.clickable {

                        when (selectedTab) {
                            0 -> {
                                if (isAllSelected) {
                                    // ❌ unselect all
                                    selectedItems.clear()
                                } else {
                                    // ✅ select all
                                    selectedItems.clear()
                                    selectedItems.addAll(historyList.map { it.id })
                                }
                            }

                            1 -> {
                                if (isAllSelectedQr) {
                                    // ❌ unselect all
                                    selectedItemsQrCode.clear()
                                } else {
                                    // ✅ select all
                                    selectedItemsQrCode.clear()
                                    selectedItemsQrCode.addAll(qrList.map { it.id })
                                }
                            }

                            2 -> {

                                if (isAllSelectedBar) {
                                    // ❌ unselect all
                                    selectedItemsBarCode.clear()
                                } else {
                                    // ✅ select all
                                    selectedItemsBarCode.clear()
                                    selectedItemsBarCode.addAll(barCodeList.map { it.id })
                                }

                            }
                        }

                    }
                )
            }


            /*if (!isBlueTickVisible) {
                AnimatedVisibility(visible = true) {
                    Image(
                        painter = painterResource(R.drawable.tick_gray_un_filled_icon), "",

                        )
                }
            } else {
                AnimatedVisibility(visible = true) {
                    Image(
                        painter = painterResource(R.drawable.tick_blue_filled_icon), ""
                    )
                }
            }*/
            AddWidth(15)
            Icon(
                painter = painterResource(R.drawable.line_icon), "",
                tint = MaterialTheme.colorScheme.secondary
            )
            AddWidth(15)
            if (!isDeleteVisible) {
                AnimatedVisibility(visible = true) {
                    Image(
                        painter = painterResource(R.drawable.del_gray_icon), "",
                        modifier = Modifier.clickable(interactionSource = null, indication = null) {
                            isDeleteVisible = true
                            isBlueTickVisible = true
                            isCrossIconVisible = true
                            isSelectedTextVisible = true
                        }
                    )
                }
            } else {
                AnimatedVisibility(visible = true) {
                    Image(
                        painter = painterResource(R.drawable.del_icon), "",
                        modifier = Modifier.clickable {
                            showDeleteSheet = true
                        }
                    )
                }
            }
        }


        // Top Bar Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {

            // Filter Chips
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
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

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(45.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceBright)
                    .clickable {
                        showSortSheet = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.sort_icon),
                    "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            AddWidth(10)


        }

        when (selectedTab) {
            0 -> {
                AllHistoryScreen(
                    historyList,
                    viewModel,
                    isBlueTickVisible,
                    selectedItems,
                    navController
                )
            }

            1 -> {
                AllHistoryScreen(
                    qrList,
                    viewModel,
                    isBlueTickVisible,
                    selectedItemsQrCode,
                    navController
                )
            }

            2 -> {
                AllHistoryScreen(
                    barCodeList,
                    viewModel,
                    isBlueTickVisible,
                    selectedItemsBarCode,
                    navController
                )
            }
        }
    }


}

@Composable
fun AllHistoryScreen(
    historyList: List<History>, viewModel: QrEditorViewModel, isBlueTickVisible: Boolean = false,
    selectedItems: SnapshotStateList<Int>, navController: NavController
) {

    if (historyList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {

            itemsIndexed(historyList, key = { _, item -> item.id }) { index, item ->

                val isSelected = selectedItems.contains(item.id)

                HistoryItem(
                    isBlueTickVisible = isBlueTickVisible,
                    isSelected = isSelected,
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

                    },
                    onSelectClick = {
                        if (isSelected) {
                            selectedItems.remove(item.id)
                        } else {
                            selectedItems.add(item.id)
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
fun HistoryItem(
    isBlueTickVisible: Boolean = false,
    isSelected: Boolean = false,
    item: History,
    onFavClick: () -> Unit,
    onItemClick: () -> Unit,
    onSelectClick: () -> Unit
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

            if (isBlueTickVisible) {
                //tick selection
                /*Image(
                    painter = painterResource(R.drawable.tick_gray_un_filled_icon),
                    contentDescription = "Favourite",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable {
                            //onFavClick()
                        }
                )*/

                Image(
                    painter = if (isSelected) {
                        painterResource(R.drawable.tick_blue_filled_icon)
                    } else {
                        painterResource(R.drawable.tick_gray_un_filled_icon)
                    },
                    contentDescription = "Select",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable {
                            onSelectClick()
                        }
                )
            } else {

                Icon(
                    painter = if (item.isFavourite) {
                        painterResource(R.drawable.fav_filled_icon)
                    } else {
                        painterResource(R.drawable.fav_un_filled_icon)
                    },
                    contentDescription = "History",
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onFavClick() },
                    tint = Color.Unspecified
                )
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    onDismiss: () -> Unit,
    onApply: (sortBy: String, order: String, orderDate: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedSortBy by remember { mutableStateOf("Name") }  // "Name" or "Date"
    var selectedOrder by remember { mutableStateOf("AtoZ") }   // "AtoZ" or "ZtoA"
    var selectedOrderDate by remember { mutableStateOf("Latest") }   // "AtoZ" or "ZtoA"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
        ) {

            // Title
            Text(
                text = "Sort By",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp),
                fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            // Name Option
            SortOptionRow(
                icon = R.drawable.note_icon,
                label = "Name",
                selected = selectedSortBy == "Name",
                onClick = { selectedSortBy = "Name" }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date Option
            SortOptionRow(
                icon = R.drawable.calendar_icon,
                label = "Date",
                selected = selectedSortBy == "Date",
                onClick = { selectedSortBy = "Date" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // A to Z / Z to A Toggle
            if (selectedSortBy == "Name") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceBright)
                        .padding(4.dp)
                )
                {
                    // A to Z
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedOrder == "AtoZ") MaterialTheme.colorScheme.onPrimary else Color.Transparent
                            )
                            .clickable { selectedOrder = "AtoZ" }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A to Z",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selectedOrder == "AtoZ") Color.White else MaterialTheme.colorScheme.secondary,
                            fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                    }

                    // Z to A
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedOrder == "ZtoA") MaterialTheme.colorScheme.onPrimary else Color.Transparent
                            )
                            .clickable { selectedOrder = "ZtoA" }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Z to A",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selectedOrder == "ZtoA") Color.White else MaterialTheme.colorScheme.secondary,
                            fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                    }
                }
            } else {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceBright)
                        .padding(4.dp)
                )
                {
                    // A to Z
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedOrderDate == "Latest") MaterialTheme.colorScheme.onPrimary else Color.Transparent
                            )
                            .clickable { selectedOrderDate = "Latest" }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Latest",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selectedOrderDate == "Latest") Color.White else Color.Gray
                        )
                    }

                    // Z to A
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedOrderDate == "Oldest") MaterialTheme.colorScheme.onPrimary else Color.Transparent
                            )
                            .clickable { selectedOrderDate = "Oldest" }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Oldest",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selectedOrderDate == "Oldest") Color.White else Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel / Apply Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Cancel
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.onBackground)
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),

                    ) {
                    Text(
                        text = "Cancel",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }

                // Apply
                Button(
                    onClick = { onApply(selectedSortBy, selectedOrder, selectedOrderDate) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3D6BF8)
                    )
                ) {
                    Text(
                        text = "Apply",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SortOptionRow(
    icon: Int,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceBright)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
            fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.onPrimary,
                unselectedColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationBottomSheet(
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Red Icon Circle
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = Color(0xFFFFEDED),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete_icon_bottom_sheet),
                    contentDescription = "Delete",
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            Text(
                text = "Confirm Deletion?",
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "These Codes will be permanently deleted and cannot be recovered.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 2.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Cancel Button

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.onBackground)
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }

                // Delete Button
                Button(
                    onClick = onConfirmDelete,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEA0C0C)
                    )
                ) {
                    Text(
                        text = "Yes, Delete",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun EmptyHistoryCard(
    modifier: Modifier = Modifier,
    fontFamily: FontFamily
) {

    Card(
        modifier = modifier
            .padding(bottom = 35.dp)
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        ),
    )

    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            // Folder Icon
            Icon(
                painter = painterResource(id = R.drawable.empty_search_icon), // Replace with your icon
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFFB0B0B0)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Main Title
            Text(
                text = "Nothing Here Yet",
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sub-description
            Text(
                text = "Start scanning to build your history",
                fontFamily = fontFamily,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            AddHeight(15)
            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(56.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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

                    Text(
                        text = "Start Scanning",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        modifier = Modifier.padding(start = 1.dp, end = 1.dp)
                    )

                }
            }

            AddHeight(10)


        }
    }
}