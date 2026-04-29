package com.example.qrcodeapp.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext



private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF101828),
    onPrimary =Color(0xFF4268FF),
    onPrimaryContainer = Color(0xFFD8D8D8),
    secondary = Color(0xFF6C727F),
    tertiary = Color(0xFFDFDFDF),
    background = Color(0xFFF6F6F6),
    onBackground = Color(0xFFFFFFFF),
    surfaceBright = Color(0xFFFFFFFF),
    outline = Color(0xFFDFDFDF)


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF4268FF),
    onPrimaryContainer = Color(0xFF343434),
    secondary = Color(0xFF999999),
    tertiary = Color(0xFF6C727F),
    background = Color(0xFF222222),
    onBackground = Color(0xFF222222),
    surfaceBright = Color(0xFF2F2F2F),
    outline = Color(0xFF444444)
    )

@Composable
fun QrCodeAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {

        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme

    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}