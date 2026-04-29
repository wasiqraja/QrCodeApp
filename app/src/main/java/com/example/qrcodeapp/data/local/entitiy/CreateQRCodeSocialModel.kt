package com.example.qrcodeapp.data.local.entitiy

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

data class CreateQRCodeSocialModel(val detailIcon:Int, val heading: String, val subTitle: String,val iconColorLight: Color?, val iconColorDark: Color?, val resId:Int, val title: String)
