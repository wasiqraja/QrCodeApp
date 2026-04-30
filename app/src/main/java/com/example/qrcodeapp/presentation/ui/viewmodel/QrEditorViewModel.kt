package com.example.qrcodeapp.presentation.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.Constants.isWebsite
import com.example.qrcodeapp.core.utils.logEvent
import com.example.qrcodeapp.core.utils.model.Dots
import com.example.qrcodeapp.core.utils.model.Eyes
import com.example.qrcodeapp.core.utils.model.LogoType
import com.example.qrcodeapp.data.dto.QrType
import com.example.qrcodeapp.data.local.entitiy.CreateQRCodeSocialModel
import com.example.qrcodeapp.domain.model.ApplyParam
import com.example.qrcodeapp.domain.model.BackgroundColor
import com.example.qrcodeapp.domain.model.BarCodeModel
import com.example.qrcodeapp.domain.model.CameraModel
import com.example.qrcodeapp.domain.model.ForegroundColor
import com.example.qrcodeapp.domain.model.Gradient
import com.example.qrcodeapp.domain.model.History
import com.example.qrcodeapp.domain.model.QrCode
import com.example.qrcodeapp.domain.model.ValidationResult
import com.example.qrcodeapp.domain.usecase.ApplyTemplateUseCase
import com.example.qrcodeapp.domain.usecase.BarCodeShowUseCase
import com.example.qrcodeapp.domain.usecase.CameraUseCase
import com.example.qrcodeapp.domain.usecase.DeleteHistoryUseCase
import com.example.qrcodeapp.domain.usecase.FavouriteUseCase
import com.example.qrcodeapp.domain.usecase.FetchHistoryCreateUseCase
import com.example.qrcodeapp.domain.usecase.FetchHistoryUseCase
import com.example.qrcodeapp.domain.usecase.GenerateBarCodeUseCase
import com.example.qrcodeapp.domain.usecase.PrepareQrDataUseCase
import com.example.qrcodeapp.domain.usecase.SaveHistoryCreateUseCase
import com.example.qrcodeapp.domain.usecase.SaveHistoryUseCase
import com.github.alexzhirkevich.customqrgenerator.QrCodeGenerator
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.QrOptions
import com.github.alexzhirkevich.customqrgenerator.ThreadPolicy
import com.github.alexzhirkevich.customqrgenerator.createQrOptions
import com.github.alexzhirkevich.customqrgenerator.style.BitmapScale
import com.github.alexzhirkevich.customqrgenerator.style.DrawableSource
import com.github.alexzhirkevich.customqrgenerator.style.QrBallShape
import com.github.alexzhirkevich.customqrgenerator.style.QrColor
import com.github.alexzhirkevich.customqrgenerator.style.QrFrameShape
import com.github.alexzhirkevich.customqrgenerator.style.QrLogoPadding
import com.github.alexzhirkevich.customqrgenerator.style.QrLogoShape
import com.github.alexzhirkevich.customqrgenerator.style.QrPixelShape
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrEditorViewModel(
    private val applyTemplateUseCase: ApplyTemplateUseCase,
    private val barCodeShowUseCase: BarCodeShowUseCase,
    private val prepareQrDataUseCase: PrepareQrDataUseCase,
    private val generateBarCodeUseCase: GenerateBarCodeUseCase,
    private val saveHistoryUseCase: SaveHistoryUseCase,
    private val fetchHistoryUseCase: FetchHistoryUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val saveHistoryCreateUseCase: SaveHistoryCreateUseCase,
    private val fetchHistoryCreateUseCase: FetchHistoryCreateUseCase,
    private val favouriteUseCase: FavouriteUseCase,
    private val cameraUseCase: CameraUseCase,
    application: Application
) : AndroidViewModel(application) {

    private var lastInsertedId: Long = -1

    private var qrGenerator: QrCodeGenerator? = null

    var selectedLogo: LogoType = LogoType.NONE
    var selectedDots: Dots = Dots.DEFAULT
    var selectedEyes: Eyes = Eyes.SQUARE_SQUARE

    // 🔥 State for Compose
    private val _qrBitmap = MutableStateFlow<Bitmap?>(null)
    val qrBitmap: StateFlow<Bitmap?> = _qrBitmap


    private lateinit var qrData: QrData.Text


    private val _barCodeBitmap = MutableStateFlow<Bitmap?>(null)
    val barCodeBitmap: StateFlow<Bitmap?> = _barCodeBitmap

    private val _qrBarCodeData = MutableStateFlow<String?>(null)
    val qrBarCodeData = _qrBarCodeData


    private val _cameraText = MutableStateFlow<String?>(null)
    val cameraText = _cameraText

    private val _camera = MutableStateFlow<androidx.camera.core.Camera?>(null)
    val camera: StateFlow<androidx.camera.core.Camera?> = _camera

    private val _qrCodeList = MutableStateFlow<List<QrCode>>(emptyList())
    val qrCodeList: StateFlow<List<QrCode>> = _qrCodeList

    private val _historyList = MutableStateFlow<List<History>>(emptyList())
    val historyList: StateFlow<List<History>> = _historyList


    val qrList: StateFlow<List<History>> = _historyList
        .map { list -> list.filter { it.type == "QR" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val barcodeList: StateFlow<List<History>> = _historyList
        .map { list -> list.filter { it.type == "BARCODE" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val qrListFav: StateFlow<List<History>> = _historyList
        .map { list -> list.filter { it.type == "QR" && it.isFavourite } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val barcodeListFav: StateFlow<List<History>> = _historyList
        .map { list -> list.filter { it.type == "BARCODE" && it.isFavourite } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



    private val _socialModel = MutableStateFlow<CreateQRCodeSocialModel?>(null)
    val socialModel: StateFlow<CreateQRCodeSocialModel?> = _socialModel


    private val _historyCreateList = MutableStateFlow<List<History>>(emptyList())
    val historyCreateList: StateFlow<List<History>> = _historyCreateList

    init {

        val threadPolicy = when (Runtime.getRuntime().availableProcessors()) {
            in 1..3 -> ThreadPolicy.SingleThread
            in 4..6 -> ThreadPolicy.DoubleThread
            else -> ThreadPolicy.QuadThread
        }

        qrGenerator = QrCodeGenerator(application, threadPolicy)
    }


    fun resetText() {
        _cameraText.value = null
    }

    fun setText(text: String?) {
        _cameraText.value = text
    }

    fun setSocialModel(model: CreateQRCodeSocialModel) {
        _socialModel.value = model
    }

    suspend fun initQrDrawable(text: String, callback: (Bitmap?) -> Unit) {

        //qrData = QrData.Text("https://raja.com/")
        qrData = QrData.Text(text)
        val options = createQrOptions(1024, 1024, .125f) {}
        val bitmap = qrGenerator?.generateQrCodeSuspend(qrData, options)
        callback(bitmap)
    }

    suspend fun initBarcodeDrawable(text: String, callback: (Bitmap?) -> Unit) {
        val bitmap = withContext(Dispatchers.Default) {
            try {
                val bitMatrix = MultiFormatWriter().encode(
                    text,
                    BarcodeFormat.CODE_128,
                    1024,
                    1024
                )
                BarcodeEncoder().createBitmap(bitMatrix)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        callback(bitmap)
    }

    fun fetchBarCodes() {
        viewModelScope.launch(Dispatchers.IO) {
            _qrCodeList.value = barCodeShowUseCase.invoke()
        }
    }

    suspend fun callBarCodeUseCase(barCodeModel: BarCodeModel) {
        _barCodeBitmap.value = generateBarCodeUseCase.invoke(barCodeModel)
    }


    fun initCamera(cameraModel: CameraModel) {
        viewModelScope.launch {
            _camera.value = cameraUseCase.initCamera(cameraModel).camera


            //Log.d("TAGCAM", "initCamera: ${cam.camera}")
        }
    }

    fun takeImage(cameraModel: CameraModel) {
        viewModelScope.launch {
            _cameraText.value = cameraUseCase.takeImage(cameraModel).data
        }
    }

    fun getTextFromImage(
        scanner: BarcodeScanner, uri: Uri, context: Context,
        onResult: (String?) -> Unit
    ) {
        val inputImage = InputImage.fromFilePath(context, uri)
        Log.d("VTAG", "display value=> TOP: ")

        scanner.process(inputImage).addOnSuccessListener { barcode ->
            if (barcode.isNotEmpty()) {
                Log.d("VTAG", "display value=> $barcode: ")
                onResult(barcode[0].displayValue)
            }
        }.addOnFailureListener {
            Log.d("VTAG", "display value=> FAILURE: ")
            onResult(null)
        }
    }

    private fun checkScannedBarcode(code: Barcode, onResult: (String?) -> Unit) {


        Log.d("VTAG", "https://www.google.com/search?q=${code.displayValue} ")
        if ((code.displayValue?.isWebsite() == true || code.valueType == 5)) {
            val isBarcode = code.valueType == 5
            val url =
                if (code.valueType == 5) "https://www.google.com/search?q=${code.displayValue}"
                else code.displayValue
            Log.d("TAGHJ", "${isBarcode} ${url}")

            onResult(url)

        } else {
            onResult(null)
        }

    }

    fun startCameraUseCase(cameraModel: CameraModel) {
        viewModelScope.launch {
            _cameraText.value = cameraUseCase(cameraModel).data
        }
    }

    fun checkBarCode() {
        val result = prepareQrDataUseCase.invoke(
            QrType.WiFi, mapOf(
                "ssid" to "wasiq",
                "lName" to "raja",
                "phone" to "03185236435"
            )
        )

        viewModelScope.launch {

            result.collect { validation ->

                when (validation) {

                    ValidationResult.Invalid("error") -> {
                        logEvent("invalid")
                    }

                    is ValidationResult.Valid -> {
                        val data = validation.data
                        _qrBarCodeData.value = data
                        logEvent(data)
                    }

                    else -> {
                        logEvent("mns")
                    }
                }
            }
        }


    }

    fun useCase1() {

        viewModelScope.launch(Dispatchers.IO) {
            val options = applyTemplateUseCase(
                //for pre-color image
                /*ApplyParam(
                    -1, Dots.RHOMBUS, Eyes.SQUARE_RHOMBUS,
                    ForegroundColor(null),
                    BackgroundColor(solidColor = null,gradientColor = Gradient(startColor = "#FFF75274", endColor = "#FFD53692",-1))
                )*/

                ApplyParam(
                    -1, Dots.RHOMBUS, Eyes.SQUARE_RHOMBUS,
                    ForegroundColor(null),
                    BackgroundColor(solidColor = "#ffffff")
                )
            ).qrOptions
            val bitmap = qrGenerator?.generateQrCodeSuspend(qrData, options)
            _qrBitmap.value = bitmap
        }
    }


    fun applyGradientUseCase() {

        viewModelScope.launch(Dispatchers.IO) {
            val options = applyTemplateUseCase(
                ApplyParam(
                    -1, Dots.RHOMBUS, Eyes.SQUARE_RHOMBUS,
                    ForegroundColor(
                        null,
                        Gradient(
                            startColor = "#37DB47",
                            endColor = "#E4DFAD",
                            R.drawable.qr_background_3,
                            null
                        )
                    ),
                    BackgroundColor(solidColor = "#16ADA8")
                )
            ).qrOptions
            val bitmap = qrGenerator?.generateQrCodeSuspend(qrData, options)
            _qrBitmap.value = bitmap
        }
    }

    fun applyPictureUseCase() {

        viewModelScope.launch(Dispatchers.IO) {
            val options = applyTemplateUseCase(
                ApplyParam(
                    -1, Dots.RHOMBUS, Eyes.SQUARE_RHOMBUS,
                    ForegroundColor(
                        null,
                        Gradient(
                            startColor = "#FF0000",
                            endColor = "#FF0000",
                            R.drawable.qr_background_3,
                            null
                        )
                    ),
                    BackgroundColor(imageDrawable = R.drawable.qr_background_3)
                )
            ).qrOptions
            val bitmap = qrGenerator?.generateQrCodeSuspend(qrData, options)
            _qrBitmap.value = bitmap
        }
    }

    fun applyLogoUseCase() {

        viewModelScope.launch(Dispatchers.IO) {

            val options = applyTemplateUseCase(
                ApplyParam(
                    R.drawable.ic_youtube, Dots.RHOMBUS, Eyes.SQUARE_RHOMBUS,
                    ForegroundColor(
                        null, Gradient(
                            startColor = "#FF0000", endColor = "#FF0000",
                            R.drawable.qr_background_3, null
                        )
                    ),
                    BackgroundColor(imageDrawable = R.drawable.qr_background_3)
                )
            ).qrOptions

            val bitmap = qrGenerator?.generateQrCodeSuspend(qrData, options)
            _qrBitmap.value = bitmap
        }
    }


    fun save(history: History) {
        viewModelScope.launch {
            try {
                lastInsertedId = saveHistoryUseCase(history)
            } catch (e: IllegalArgumentException) {
                // handle validation error → show to UI
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            deleteHistoryUseCase(lastInsertedId)
        }
    }

    fun favourite(isFav: Boolean, favStatus: (Boolean) -> Unit) {
        viewModelScope.launch {
            val status=favouriteUseCase(lastInsertedId.toInt(), isFav)
            //favStatus.invoke(status)
            favStatus(status)
        }
    }

    fun updateFav(id:Int){
        viewModelScope.launch {
            favouriteUseCase(id,true)
        }
    }


    fun fetchHistory() {
        viewModelScope.launch {
            fetchHistoryUseCase.invoke().collectLatest {
                _historyList.value = it
            }
        }
    }


    fun saveCreate(resId: Int, data: String, dateCreated: Long) {
        viewModelScope.launch {
            try {
                saveHistoryCreateUseCase(resId = resId, data = data, dateCreated = dateCreated)
            } catch (e: IllegalArgumentException) {
                // handle validation error → show to UI
            }
        }
    }

    fun fetchHistoryCreate() {
        viewModelScope.launch {
            fetchHistoryCreateUseCase.invoke().collectLatest {
                logEvent("size is => ${it.size}")
                _historyCreateList.value = it
            }
        }
    }


    fun applyTemplate(
        templateIndex: Int,
        @DrawableRes
        logoRes: Int? = -1,
        foreGroundColor: ForegroundColor?, backgroundColor: BackgroundColor?
    ) {

        //val template = getTemplate(templateIndex) ?: return

        viewModelScope.launch(Dispatchers.IO) {


            val options = generateOptions(
                logoRes!!,
                Dots.RHOMBUS,
                Eyes.SQUARE_RHOMBUS,
                backgroundColor,
                foreGroundColor,

                )

            val bitmap = qrGenerator?.generateQrCodeSuspend(qrData, options)

            _qrBitmap.value = bitmap
        }
    }


    private fun generateOptions(
        logoImage: Int = -1,
        dotsShape: Dots = Dots.DEFAULT,
        eyesShape: Eyes = Eyes.SQUARE_RHOMBUS,
        backgroundColor: BackgroundColor? = null,
        foreGroundColor: ForegroundColor? = null,
        logoUri: String? = null
    ): QrOptions {

        return createQrOptions(1024, 1024, .125f) {
            if (logoImage != -1)
                logo {
                    drawable = if (logoUri == null)
                        DrawableSource.Resource(logoImage)
                    else
                        DrawableSource.File(logoUri)
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
                    Log.d("TAGgb", "generateOptions:dfdf ")
                    /*background {
                        Log.d("TAGgb", "generateOptions: ")
                        drawable = DrawableSource.Resource(com.example.qrcodeapp.R.drawable.qr_background_3)
                    }*/
                }
            }


        }
    }


    private fun getTemplate(index: Int): Triple<ForegroundColor, BackgroundColor, Eyes>? {
        val templates = listOf(
            Triple(ForegroundColor("#000000"), BackgroundColor(), Eyes.SQUARE_SQUARE),
            Triple(ForegroundColor("#18183B"), BackgroundColor(), Eyes.OVAL_OVAL),
            Triple(ForegroundColor("#D99C95"), BackgroundColor("#F2F2F2"), Eyes.OVAL_OVAL),
            Triple(ForegroundColor("#FFFFFF"), BackgroundColor("#217E93"), Eyes.OVAL_OVAL),
            Triple(ForegroundColor("#000000"), BackgroundColor(), Eyes.OVAL_OVAL),
            Triple(
                ForegroundColor("#072359"),
                BackgroundColor(
                    "#000000",
                    gradientColor = Gradient(
                        startColor = "#37DB47",
                        endColor = "#E4DFAD",
                        R.drawable.qr_background_3,
                        null
                    )
                ),
                Eyes.SQUARE_SQUARE
            ),
            Triple(ForegroundColor(), BackgroundColor(), Eyes.SQUARE_SQUARE),
            Triple(ForegroundColor("#ffffff"), BackgroundColor("#FF0000"), Eyes.SQUARE_SQUARE),
            Triple(ForegroundColor(), BackgroundColor(), Eyes.OVAL_OVAL),
            Triple(ForegroundColor(), BackgroundColor(), Eyes.OVAL_OVAL),
            Triple(ForegroundColor("#ffffff"), BackgroundColor("#33CCFF"), Eyes.SQUARE_SQUARE),
            Triple(ForegroundColor("#FF0000"), BackgroundColor(), Eyes.SQUARE_SQUARE),
            Triple(ForegroundColor("#072359"), BackgroundColor("#DFFCE2"), Eyes.OVAL_OVAL),
            Triple(ForegroundColor("#000000"), BackgroundColor("#FFE977"), Eyes.OVAL_OVAL),
            Triple(ForegroundColor("#ffffff"), BackgroundColor("#5874A9"), Eyes.OVAL_OVAL),
            Triple(ForegroundColor("#ffffff"), BackgroundColor("#2259C7"), Eyes.SQUARE_SQUARE),
            Triple(ForegroundColor("#8a2fff"), BackgroundColor("#FFFFFF"), Eyes.OVAL_OVAL)
        )
        return templates.getOrNull(index)
    }
}