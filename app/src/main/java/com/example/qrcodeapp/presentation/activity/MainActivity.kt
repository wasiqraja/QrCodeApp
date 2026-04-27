package com.example.qrcodeapp.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qrcodeapp.presentation.navigation.OnBoardingScreen
import com.example.qrcodeapp.presentation.navigation.SplashScreen
import com.example.qrcodeapp.presentation.ui.screens.BarCodeListScreen
import com.example.qrcodeapp.presentation.ui.screens.CameraScreen
import com.example.qrcodeapp.presentation.ui.screens.EditQrCodeScreen
import com.example.qrcodeapp.presentation.ui.screens.LanguageScreen
import com.example.qrcodeapp.presentation.ui.screens.OnBoardScreen
import com.example.qrcodeapp.presentation.ui.theme.QrCodeAppTheme
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            QrCodeAppTheme {
                val viewModel: QrEditorViewModel = koinViewModel()
                //CameraScreen(viewModel){}
                //EditQrCodeScreen( viewModel)

                //BarCodeListScreen(viewModel)

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = SplashScreen
                ){
                    composable <SplashScreen>{
                       // com.example.qrcodeapp.presentation.ui.screens.SplashScreen(viewModel,navController)
                        //EditQrCodeScreen( viewModel)
                        LanguageScreen{}
                    }

                    composable <OnBoardingScreen>{
                        OnBoardScreen()
                    }
                }




            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QrCodeAppTheme {
        Greeting("Android")
    }
}