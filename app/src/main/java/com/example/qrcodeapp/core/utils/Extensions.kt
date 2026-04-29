package com.example.qrcodeapp.core.utils

import android.util.Log
import androidx.camera.core.Camera
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



fun logEvent(msg: String){
    Log.d("TAGEVENT", "${msg}")
}


@Composable
fun AddWidth(width: Int) {
    Spacer(modifier = Modifier.width(width.dp))
}


@Composable
fun AddHeight(height: Int) {
    Spacer(modifier = Modifier.height(height.dp))
}
