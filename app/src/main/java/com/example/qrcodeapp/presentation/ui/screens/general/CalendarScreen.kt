package com.example.qrcodeapp.presentation.ui.screens.general

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.AddHeight
import com.example.qrcodeapp.presentation.ui.theme.QrCodeAppTheme


@Composable
fun CalendarScreen() {


    var orgName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val albertSansMedium = FontFamily(Font(R.font.visbycf_medium))


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
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.bak_arrow_icon),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.calendar),
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
            Spacer(modifier = Modifier.height(15.dp))

            Card(
                modifier = Modifier.size(90.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF337FFF))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(38.dp))

            // Subject Input
            CalendarInputFieldContact(
                value = orgName,
                onValueChange = { orgName = it },
                placeholder = stringResource(R.string.organization),
                leadingIconRes = R.drawable.org_icon,
                fontFamily = albertSansMedium
            )

            Spacer(modifier = Modifier.height(10.dp))


            CalendarInputFieldPhone(
                value = startDate,
                onValueChange = { startDate = it },
                placeholder = stringResource(R.string.start_date),
                leadingIconRes = R.drawable.calendar_icon,
                fontFamily = albertSansMedium
            ){
                openDialog.value=true
            }



            Spacer(modifier = Modifier.height(10.dp))

            CalendarInputFieldContact(
                value = location,
                onValueChange = { location = it },
                placeholder = stringResource(R.string.location),
                leadingIconRes = R.drawable.loc_icon,
                fontFamily = albertSansMedium
            )

            Spacer(modifier = Modifier.height(10.dp))


            // Body Text Input
            CalendarInputFieldContact(
                value = description,
                onValueChange = { description = it },
                placeholder = stringResource(R.string.des),
                leadingIconRes = R.drawable.email_sub_icon,
                singleLine = false,
                modifier = Modifier.height(120.dp),
                fontFamily = albertSansMedium,
                isTopAligned = true // Align icon to top for multi-line field
            )

            Spacer(modifier = Modifier.height(14.dp))


            if (openDialog.value) {
                DatePickerDialog(
                    onDismissRequest = { openDialog.value = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { openDialog.value = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

        }

        if (orgName.isNotEmpty() && location.isNotEmpty() && description.isNotEmpty() && startDate.isNotEmpty()
            && endDate.isNotEmpty()
        ) {
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
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
                    .height(54.dp),
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
                        tint = Color(0xFF6E6E6E).copy(alpha = .7f)
                    )
                }
            }
        }

        AddHeight(10)
        Image(
            painter = painterResource(R.drawable.screen_bottom__line_bar_icon), "",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        AddHeight(5)
    }
}


@Composable
fun CalendarInputFieldContact(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIconRes: Int,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    isTopAligned: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = .9f),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 2.dp)
            )
        },
        leadingIcon = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = if (isTopAligned) 16.dp else 0.dp),
                contentAlignment = if (isTopAligned) Alignment.TopCenter else Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = leadingIconRes),
                    contentDescription = null,
                    tint = if (value.isEmpty()) {
                        MaterialTheme.colorScheme.secondary.copy(alpha = .7f)
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        )
    )
}


@Composable
fun CalendarInputFieldPhone(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIconRes: Int,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    isTopAligned: Boolean = false,
    onClick:()-> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        /*TextField(
            value = value,
            onValueChange = { },          // ← empty, no input accepted
            readOnly = true,              // ← blocks typing, no keyboard opens
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = .9f),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 2.dp)
                )
            },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = if (isTopAligned) 16.dp else 0.dp),
                    contentAlignment = if (isTopAligned) Alignment.TopCenter else Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = leadingIconRes),
                        contentDescription = null,
                        tint = if (value.isEmpty()) {
                            MaterialTheme.colorScheme.secondary.copy(alpha = .5f)
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            singleLine = singleLine,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.Transparent   // ← cursor invisible
            )
        )*/

        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceBright)
                .clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = if (isTopAligned) Alignment.Top else Alignment.CenterVertically
        ) {

            // Leading Icon
            Icon(
                painter = painterResource(id = leadingIconRes),
                contentDescription = null,
                tint = if (value.isEmpty()) {
                    MaterialTheme.colorScheme.secondary.copy(alpha = .5f)
                } else {
                    MaterialTheme.colorScheme.onPrimary
                },
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = if (isTopAligned) 4.dp else 0.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Text / Placeholder
            Text(
                text = value.ifEmpty { placeholder },
                color = if (value.isEmpty()) {
                    MaterialTheme.colorScheme.tertiary.copy(alpha = .9f)
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
fun DarkPreview() {
    QrCodeAppTheme(darkTheme = false) {
        CalendarScreen()
    }
}
