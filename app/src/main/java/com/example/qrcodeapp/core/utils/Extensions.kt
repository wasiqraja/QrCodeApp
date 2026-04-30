package com.example.qrcodeapp.core.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.qrcodeapp.R
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import kotlin.time.Instant
import androidx.core.net.toUri

val imageAnalysis = ImageAnalysis.Builder()
    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
    .build()

val imageCapture = ImageCapture.Builder()
    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
    .build()


fun Activity.copyToClipboard(value: String) {
    val clipboard =
        getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("QR copy", value)
    clipboard.setPrimaryClip(clip)
    showToast("Link Copied!")
}

fun Activity.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}


fun Activity.shareQR(bitmap: Bitmap) {

    if (!bitmap.isRecycled) {
        val imgName = "${UUID.randomUUID()}.png"
        val cacheFile = File(externalCacheDir, imgName)
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = FileOutputStream(cacheFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

        } catch (e: java.lang.Exception) {
            Log.i("TAG", "shareQrCode: ", e)
        }

        val uri = FileProvider.getUriForFile(this, "$packageName.provider", cacheFile)
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        val finalText =
            "Scanned Using ${getString(R.string.app_name)} https://play.google.com/store/apps/details?id=${packageName}"
        intent.putExtra(Intent.EXTRA_TEXT, finalText)
        intent.type = "image/png"
        startActivityForResult(
            Intent.createChooser(intent, "Share With"),
            999
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM • hh:mm a", Locale.ENGLISH)
    return LocalDateTime.now().format(formatter)
}


fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}


fun logEvent(msg: String) {
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
