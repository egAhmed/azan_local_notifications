package com.ahmed.azanlocalnotifications.utils

import androidx.annotation.Keep

@Keep
object StringUtils {
    fun isNullOrEmpty(string: String?): Boolean {
        return string == null || string.isEmpty()
    }
}