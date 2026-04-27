package com.example.qrcodeapp.core.utils

import androidx.lifecycle.MutableLiveData


object Constants {
    val canUserChangeScreen: MutableLiveData<Boolean> = MutableLiveData(true)

    fun String.isWebsite(): Boolean {
        if (startsWith("http://") || startsWith("https://")) {
            return true
        }
        return false
    }

}