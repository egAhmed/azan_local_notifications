package com.ahmed.azanlocalnotifications.utils

import androidx.annotation.Keep

@Keep
object BooleanUtils {
    fun getValue(booleanObject: Boolean?): Boolean {
        return booleanObject != null && booleanObject.booleanValue()
    }
}