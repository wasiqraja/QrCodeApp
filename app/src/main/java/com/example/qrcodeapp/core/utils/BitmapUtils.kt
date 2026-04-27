package com.example.qrcodeapp.core.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.example.qrcodeapp.core.utils.model.Dots

object BitmapUtils {

    /**
     * Morphological dilation — expands dark pixels outward by [radius] pixels.
     * Use after QR generation to fatten thin shapes like Star/Rhombus.
     */
    fun dilate(src: Bitmap, radius: Int = 2): Bitmap? {
        val width = src.width
        val height = src.height
        val pixels = IntArray(width * height)
        src.getPixels(pixels, 0, width, 0, 0, width, height)

        val output = pixels.copyOf()

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = pixels[y * width + x]
                // If this pixel is dark, expand it to neighbors
                if (isDark(pixel)) {
                    for (dy in -radius..radius) {
                        for (dx in -radius..radius) {
                            val nx = x + dx
                            val ny = y + dy
                            if (nx in 0 until width && ny in 0 until height) {
                                output[ny * width + nx] = pixel
                            }
                        }
                    }
                }
            }
        }

        val result = src.config?.let { Bitmap.createBitmap(width, height, it) }
        result?.setPixels(output, 0, width, 0, 0, width, height)
        return result
    }

    /**
     * Checks if a pixel is "dark" — covers solid colors and gradient colors.
     * Excludes near-white background pixels.
     */
    private fun isDark(color: Int): Boolean {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        val a = Color.alpha(color)
        if (a < 128) return false           // transparent → background
        val luminance = 0.299f * r + 0.587f * g + 0.114f * b
        return luminance < 180f             // dark enough to be a QR dot
    }


    fun Bitmap.thickenIfNeeded(dots: Dots): Bitmap? {
        return when (dots) {
            Dots.STAR     -> BitmapUtils.dilate(this, radius = 2)
            Dots.RHOMBUS  -> BitmapUtils.dilate(this, radius = 1)
            else          -> this  // DEFAULT and CIRCLE are fine as-is
        }
    }
}