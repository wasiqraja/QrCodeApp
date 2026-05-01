package com.example.qrcodeapp.presentation.navigation

import com.example.qrcodeapp.data.local.entitiy.CreateQRCodeSocialModel
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable



@Serializable
data object SplashScreen

@Serializable
data class LanguageScreen(val id: Int)

@Serializable
data object OnBoardingScreen

@Serializable
data object HomeScreen


@Serializable
data object SocialDetailScreen

@Serializable
data object CameraScreen


@Serializable
data object QRResultScreen



@Serializable
data object BarCodeResultScreen


@Serializable
data object HistoryScreen

@Serializable
data object FavouriteScreen



