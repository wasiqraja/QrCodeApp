package com.example.qrcodeapp.core.utils.model

import com.example.qrcodeapp.R

/*
data class QrTemplate(
    var scannerRes : Int= R.drawable.sca,
    var bubbleRes : Int= R.drawable.bubble,
    var varscannerToBubbleRatio : Float = 0.4f // Scanner is smaller, Bubble is wider
)
*/

data class QrTemplate(
    val scannerRes: Int=R.drawable.sca,
    val bubbleRes: Int=R.drawable.temp_3_scan_me,
    val scannerToBubbleRatio: Float = 0.35f, // What % of width the scanner takes
    val qrPaddingScale: Float = 0.12f        // Padding inside the scanner box
)