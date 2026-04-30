package com.example.qrcodeapp.presentation.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qrcodeapp.presentation.navigation.BarCodeResultScreen
import com.example.qrcodeapp.presentation.navigation.CameraScreen
import com.example.qrcodeapp.presentation.navigation.HistoryScreen
import com.example.qrcodeapp.presentation.navigation.OnBoardingScreen
import com.example.qrcodeapp.presentation.navigation.QRResultScreen
import com.example.qrcodeapp.presentation.navigation.SocialDetailScreen
import com.example.qrcodeapp.presentation.navigation.SplashScreen
import com.example.qrcodeapp.presentation.ui.screens.CameraScreen
import com.example.qrcodeapp.presentation.ui.screens.FavoriteScreen
import com.example.qrcodeapp.presentation.ui.screens.HistoryScreen
import com.example.qrcodeapp.presentation.ui.screens.HomeScreen
import com.example.qrcodeapp.presentation.ui.screens.OnBoardScreen
import com.example.qrcodeapp.presentation.ui.screens.camera.CameraBarResultScreen
import com.example.qrcodeapp.presentation.ui.screens.camera.CameraQrResultScreen
import com.example.qrcodeapp.presentation.ui.screens.createqr.FacebookScreen
import com.example.qrcodeapp.presentation.ui.theme.QrCodeAppTheme
import com.example.qrcodeapp.presentation.ui.viewmodel.QrEditorViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
                ) {
                    composable<SplashScreen> {
                        // com.example.qrcodeapp.presentation.ui.screens.SplashScreen(viewModel,navController)
                        //EditQrCodeScreen( viewModel)

                        //CreateQRScreen(navController, QrCodeListProvider(),viewModel)
                        //QrResultScreen()
                        //EmailScreen()
                        //ClipBoardScreen()
                        //WebsiteScreen()
                        //SmsScreen()
                        //LocationScreen()
                        //WifiScreen()
                        //ContactScreen()
                        //CalendarScreen()


                        //CameraQrResultScreen()
                        HomeScreen(navController,viewModel)



                    }

                    composable<OnBoardingScreen> {
                        OnBoardScreen()
                    }

                    composable<SocialDetailScreen> {
                        FacebookScreen(viewModel)
                    }


                    composable<CameraScreen> {
                        CameraScreen(viewModel, navController)
                    }

                    composable<BarCodeResultScreen> {
                        CameraBarResultScreen(viewModel, navController)
                    }

                    composable<QRResultScreen> {
                        CameraQrResultScreen(viewModel, navController)
                    }

                    composable<HistoryScreen> {
                        //HistoryScreen(navController, viewModel)
                        FavoriteScreen(navController,viewModel)
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