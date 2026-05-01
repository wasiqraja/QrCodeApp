package com.example.qrcodeapp.presentation.ui.screens.settings

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.AddHeight
import com.example.qrcodeapp.core.utils.AddWidth


@Composable
fun SettingScreen() {

    var showSheet by remember { mutableStateOf(true) }

    if (showSheet) {
        RateUsBottomSheet(
            onDismiss = { showSheet = false },
            onSubmit = { rating ->
                showSheet = false
            }
        )
    }

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
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Settings",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.albertsans_medium)),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 2.dp)
            )
        }

        AddHeight(25)
        Text(
            text = "General",
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.albertsans_medium)),
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 16.dp)
        )


        AddHeight(10)
        Card(
            modifier = Modifier
                .clickable(interactionSource = null, indication = null) {
                }
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(painter = painterResource(R.drawable.fav_filled_icon), "")
                AddWidth(10)
                Text(
                    text = "Favourite",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(painter = painterResource(R.drawable.right_arrow_icon), "")
                AddWidth(5)


            }
        }

        AddHeight(10)
        Text(
            text = "System",
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.albertsans_medium)),
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 16.dp)
        )

        AddHeight(10)
        Card(
            modifier = Modifier
                .clickable(interactionSource = null, indication = null) {
                }
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(painter = painterResource(R.drawable.share_app_icon), "")
                AddWidth(10)
                Text(
                    text = "Share App",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(painter = painterResource(R.drawable.right_arrow_icon), "")
                AddWidth(5)


            }
        }


        AddHeight(6)
        Card(
            modifier = Modifier
                .clickable(interactionSource = null, indication = null) {
                }
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(painter = painterResource(R.drawable.rate_us_icon), "")
                AddWidth(10)
                Text(
                    text = "Rate Us",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(painter = painterResource(R.drawable.right_arrow_icon), "")
                AddWidth(5)


            }
        }


        AddHeight(6)
        Card(
            modifier = Modifier
                .clickable(interactionSource = null, indication = null) {
                }
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(painter = painterResource(R.drawable.feedback_icon), "")
                AddWidth(10)
                Text(
                    text = "Feedback",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(painter = painterResource(R.drawable.right_arrow_icon), "")
                AddWidth(5)


            }
        }


        AddHeight(6)
        Card(
            modifier = Modifier
                .clickable(interactionSource = null, indication = null) {
                }
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(painter = painterResource(R.drawable.pp_icon), "")
                AddWidth(10)
                Text(
                    text = "Privacy Policy",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.visbycf_medium)),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(painter = painterResource(R.drawable.right_arrow_icon), "")
                AddWidth(5)


            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateUsBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var rating by remember { mutableStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Top Handle
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(Color.LightGray, RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Icon Circle
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = Color(0xFFE8ECFF),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.star_filled_icon), // replace
                    contentDescription = null,
                    tint = Color(0xFF4C6FFF),
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Rate Your Experience",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Help us improve by sharing your feedback.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ⭐ Rating Stars
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(5) { index ->
                    val isSelected = index < rating

                    Icon(
                        painter = painterResource(
                            if (isSelected) R.drawable.star_filled_icon
                            else R.drawable.star_unfilled_icon
                        ),
                        contentDescription = null,
                        tint = if (isSelected) Color(0xFF4C6FFF) else Color.Gray,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                rating = index + 1
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Rate Button
            Button(
                onClick = { onSubmit(rating) },
                enabled = rating > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4C6FFF),
                    disabledContainerColor = Color.LightGray.copy(alpha = 0.4f)
                )
            ) {
                Text("Rate Us")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Later Button
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("I’ll do this later")
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}