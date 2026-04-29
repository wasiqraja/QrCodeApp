package com.example.qrcodeapp.core.utils

import androidx.camera.core.Camera
import androidx.lifecycle.MutableLiveData


object Constants {
    val canUserChangeScreen: MutableLiveData<Boolean> = MutableLiveData(true)

    var cam: Camera?=null

    fun String.isWebsite(): Boolean {
        if (startsWith("http://") || startsWith("https://")) {
            return true
        }
        return false
    }

}