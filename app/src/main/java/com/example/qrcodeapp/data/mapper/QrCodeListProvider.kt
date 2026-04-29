package com.example.qrcodeapp.data.mapper

import androidx.compose.ui.graphics.Color
import com.example.qrcodeapp.R
import com.example.qrcodeapp.data.local.entitiy.CreateQRCodeModel
import com.example.qrcodeapp.data.local.entitiy.CreateQRCodeSocialModel

class QrCodeListProvider(){

    fun getSocialList():List<CreateQRCodeSocialModel>{

        val list=mutableListOf< CreateQRCodeSocialModel>()
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.facebook_main_icon, heading = "Paste your Facebook link here to create a QR code", subTitle = "Facebook URL", iconColorLight = Color(0xFF337FFF), iconColorDark = Color(0xFF337FFF), resId = R.drawable.facebook_icon,"Facebook"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.whatsapp_main_icon, heading = "Paste your WhatsApp link here to create a QR code", subTitle = "WhatsApp URL",iconColorLight = Color(0xFF00D95F), iconColorDark = Color(0xFF337FFF),resId = R.drawable.whatsapp_icon,"WhatsApp"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.instagram_main_icon, heading = "Paste your Instagram link here to create a QR code", subTitle = "Instagram URL",iconColorLight = null, iconColorDark = null,resId = R.drawable.instagram_icon,"Instagram"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.x_main_icon, heading = "Paste your X link here to create a QR code", subTitle = "X URL",iconColorLight = Color(0xFF222222), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.x_icon,"X"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.youtube_icon, heading = "Paste your Youtube link here to create a QR code", subTitle = "Youtube URL",null, iconColorDark = null,resId = R.drawable.youtube_icon,"Youtube"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.tumbler_main_icon, heading = "Paste your Tumbler link here to create a QR code", subTitle = "Tumbler URL",iconColorLight = Color(0xFF303D4D), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.tumbler_icon,"Tumblr"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.flickr_main_icon, heading = "Paste your Flickr link here to create a QR code", subTitle = "Flickr URL",iconColorLight = null, iconColorDark = null,resId = R.drawable.flickr_icon,"Flickr"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.vimeo_main_icon, heading = "Paste your Vimeo link here to create a QR code", subTitle = "Vimeo URL",iconColorLight = null, iconColorDark = null,resId = R.drawable.vimeo_icon,"Vimeo"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.snapchat_main_icon, heading = "Paste your Snapchat link here to create a QR code", subTitle = "Snapchat URL",iconColorLight = null, iconColorDark = null,resId = R.drawable.snapchat_icon,"Snapchat"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.thread_main_icon, heading = "Paste your Thread link here to create a QR code", subTitle = "Thread URL",iconColorLight = Color(0xFF000000), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.thread_icon,"Threads"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.linkedin_main_icon, heading = "Paste your LinkedIn link here to create a QR code", subTitle = "LinkedIn URL",iconColorLight = Color(0xFF006699), iconColorDark = Color(0xFF006699),resId = R.drawable.linked_icon,"LinkedIn"))
        list.add(CreateQRCodeSocialModel(detailIcon = R.drawable.medium_main_icon, heading = "Paste your Medium link here to create a QR code", subTitle = "Medium URL",iconColorLight = Color(0xFF000000), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.medium_icon,"Medium"))

        return list
    }


    fun getGeneralList():List<CreateQRCodeModel>{

        val list=mutableListOf<CreateQRCodeModel>()
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF), resId = R.drawable.message_icon,"Email"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.wifi_icon,"Wifi"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.clip_board_icon,"Clipboard"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.website_icon,"Website"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.location_icon,"Location"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.contact_icon,"Contact"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.sms_icon,"Sms"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.calendar_icon,"Calendar"))
        return list
    }


    fun get2DBarCodeList():List<CreateQRCodeModel>{

        val list=mutableListOf<CreateQRCodeModel>()
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.data_matrix_icon,"DataMatrix"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.pdf_417_icon,"Pdf417"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.aztec_icon,"Aztec"))
        return list
    }


    fun get1DBarCodeList():List<CreateQRCodeModel>{

        val list=mutableListOf<CreateQRCodeModel>()
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.bar_code_1,"EAN13"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.bar_code_1,"EAN8"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.bar_code_1,"UPCA"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.bar_code_1,"UPCE"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.bar_code_1,"ITF"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.bar_code_1,"Code128"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.bar_code_1,"Code93"))
        list.add(CreateQRCodeModel(iconColorLight = Color(0xFF101828), iconColorDark = Color(0xFFFFFFFF),resId = R.drawable.bar_code_1,"CodeBar"))

        return list
    }
}