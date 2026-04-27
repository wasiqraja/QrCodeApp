package com.example.qrcodeapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.qrcodeapp.R
import com.example.qrcodeapp.presentation.navigation.OnBoardingScreen
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel


@Composable
fun SplashScreen(viewModel: QrEditorViewModel, navController: NavController) {

    val historyList = viewModel.historyList.collectAsState().value
    val historyCreateList = viewModel.historyCreateList.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchHistory()
        viewModel.fetchHistoryCreate()
    }


    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Scan History Add", modifier = Modifier.clickable {
            viewModel.save(R.drawable.ic_youtube, "You tube link", System.currentTimeMillis())
        })

        Text(text = "Create History Add", modifier = Modifier.clickable {
            viewModel.saveCreate(R.drawable.sca, "snap tube link", System.currentTimeMillis())
        })

        Button(onClick = {
            navController.navigate(OnBoardingScreen)
        }) {
            Text(text = "Get Started")
        }

        if (historyList.isNotEmpty()) {
            Text(
                text = historyList.size.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (historyCreateList.isNotEmpty()) {
            Text(
                text = historyCreateList.size.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Red
            )
        }
    }
}