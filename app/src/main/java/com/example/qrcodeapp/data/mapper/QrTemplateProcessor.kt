package com.example.qrcodeapp.data.mapper

import android.graphics.Color
import android.util.Log
import com.example.qrcodeapp.core.utils.model.Dots
import com.example.qrcodeapp.core.utils.model.Eyes
import com.example.qrcodeapp.domain.model.TemplateResult
import com.example.qrcodeapp.domain.model.ApplyParam
import com.github.alexzhirkevich.customqrgenerator.createQrOptions
import com.github.alexzhirkevich.customqrgenerator.style.BitmapScale
import com.github.alexzhirkevich.customqrgenerator.style.DrawableSource
import com.github.alexzhirkevich.customqrgenerator.style.QrBallShape
import com.github.alexzhirkevich.customqrgenerator.style.QrColor
import com.github.alexzhirkevich.customqrgenerator.style.QrFrameShape
import com.github.alexzhirkevich.customqrgenerator.style.QrLogoPadding
import com.github.alexzhirkevich.customqrgenerator.style.QrLogoShape
import com.github.alexzhirkevich.customqrgenerator.style.QrPixelShape

class QrTemplateProcessor {

    fun process(param: ApplyParam): TemplateResult {

        param.run {

            var selectedDots: Dots = Dots.DEFAULT
            var selectedEyes: Eyes = Eyes.SQUARE_SQUARE

            Log.d(
                "TAG4",
                "applyTemplate: logo=${logoImage}\ndots=${dotsShape}\neyesshape${eyesShape}\nbackground${backgroundColor}\n" + "foreground${foreGroundColor}\nlogoUri${logoUri}"
            )

            val options = createQrOptions(1024, 1024, .125f) {

                if (logoImage != -1) logo {
                    drawable = if (logoUri == null) DrawableSource.Resource(logoImage)
                    else DrawableSource.File(logoUri)
                    size = .20f
                    padding = QrLogoPadding.Natural(.1f)
                    shape = QrLogoShape.Circle
                }

                shapes {
                    darkPixel = when (dotsShape) {
                        Dots.DEFAULT -> {
                            selectedDots = Dots.DEFAULT
                            QrPixelShape.Default
                        }

                        Dots.CIRCLE -> {
                            selectedDots = Dots.CIRCLE
                            QrPixelShape.Circle()
                        }

                        Dots.RHOMBUS -> {
                            selectedDots = Dots.RHOMBUS
                            QrPixelShape.Rhombus
                        }

                        Dots.STAR -> {
                            selectedDots = Dots.STAR
                            QrPixelShape.Star
                        }
                    }

                    when (eyesShape) {
                        Eyes.SQUARE_SQUARE -> {
                            frame = QrFrameShape.Default
                            ball = QrBallShape.Default
                            selectedEyes = Eyes.SQUARE_SQUARE
                        }

                        Eyes.SQUARE_CIRCLE -> {
                            frame = QrFrameShape.Default
                            ball = QrBallShape.Circle()
                            selectedEyes = Eyes.SQUARE_CIRCLE
                        }

                        Eyes.SQUARE_OVAL -> {
                            frame = QrFrameShape.Default
                            ball = QrBallShape.RoundCorners(0.2f)
                            selectedEyes = Eyes.SQUARE_OVAL
                        }

                        Eyes.SQUARE_RHOMBUS -> {
                            frame = QrFrameShape.Default
                            ball = QrBallShape.Rhombus
                            selectedEyes = Eyes.SQUARE_RHOMBUS
                        }

                        Eyes.CIRCLE_CIRCLE -> {
                            frame = QrFrameShape.Circle()
                            ball = QrBallShape.Circle()
                            selectedEyes = Eyes.CIRCLE_CIRCLE
                        }

                        Eyes.CIRCLE_RHOMBUS -> {
                            frame = QrFrameShape.Circle()
                            ball = QrBallShape.Rhombus
                            selectedEyes = Eyes.CIRCLE_RHOMBUS
                        }

                        Eyes.CIRCLE_OVAL -> {
                            frame = QrFrameShape.Circle()
                            ball = QrBallShape.RoundCorners(0.2f)
                            selectedEyes = Eyes.CIRCLE_OVAL
                        }

                        Eyes.OVAL_OVAL -> {
                            frame = QrFrameShape.RoundCorners(0.2f)
                            ball = QrBallShape.RoundCorners(0.2f)
                            selectedEyes = Eyes.OVAL_OVAL
                        }

                        Eyes.OVAL_CIRCLE -> {
                            frame = QrFrameShape.RoundCorners(0.2f)
                            ball = QrBallShape.Circle()
                            selectedEyes = Eyes.OVAL_CIRCLE
                        }

                        Eyes.OVAL_RHOMBUS -> {
                            frame = QrFrameShape.RoundCorners(0.2f)
                            ball = QrBallShape.Rhombus
                            selectedEyes = Eyes.OVAL_RHOMBUS
                        }
                    }
                }

                colors {

                    if (foreGroundColor?.solidColor != null) {
                        dark = QrColor.Solid(Color.parseColor(foreGroundColor.solidColor))
                    }

                    if (backgroundColor?.solidColor != null) {
                        background {
                            color = QrColor.Solid(Color.parseColor(backgroundColor.solidColor))
                        }
                    }

                    if (foreGroundColor?.gradientColor != null) {
                        dark = QrColor.LinearGradient(
                            startColor = Color.parseColor(foreGroundColor.gradientColor!!.endColor),
                            endColor = Color.parseColor(foreGroundColor.gradientColor!!.startColor),
                            QrColor.LinearGradient.Orientation.Vertical
                        )
                    }

                    if (backgroundColor?.gradientColor != null) {
                        background {
                            color = QrColor.LinearGradient(
                                startColor = Color.parseColor(backgroundColor.gradientColor!!.endColor),
                                endColor = Color.parseColor(backgroundColor.gradientColor!!.startColor),
                                QrColor.LinearGradient.Orientation.Vertical
                            )
                        }
                    }

                    if (backgroundColor?.imageUri != null) {
                        Log.d("TAGgb", "generateOptions: ")
                        background {
                            scale = BitmapScale.CenterCrop
                            drawable = DrawableSource.File(backgroundColor.imageUri!!)
                        }
                    }

                    if (backgroundColor?.imageDrawable != null) {
                        background {
                            Log.d("TAGgb", "generateOptions: ")
                            drawable =
                                DrawableSource.Resource(com.example.qrcodeapp.R.drawable.qr_background_3)
                        }
                    } else {
                        Log.d("TAGgb", "generateOptions:dfdf ")/*background {
                            Log.d("TAGgb", "generateOptions: ")
                            drawable = DrawableSource.Resource(com.example.qrcodeapp.R.drawable.qr_background_3)
                        }*/
                    }
                }


            }

            return TemplateResult(
                qrOptions = options,
                selectedDots = selectedDots,
                selectedEyes = selectedEyes,
                templateName = "Custom Template"
            )
        }
    }
}