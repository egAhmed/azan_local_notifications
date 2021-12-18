package com.ahmed.azanlocalnotifications.models

import androidx.annotation.Keep
import java.io.Serializable
import java.util.Map

@Keep
class Time : Serializable {
    var hour: Integer? = 0
    var minute: Integer? = 0
    var second: Integer? = 0

    companion object {
        private const val HOUR = "hour"
        private const val MINUTE = "minute"
        private const val SECOND = "second"
        fun from(arguments: Map<String?, Object?>): Time {
            val time = Time()
            time.hour = arguments[HOUR] as Integer?
            time.minute = arguments[MINUTE] as Integer?
            time.second = arguments[SECOND] as Integer?
            return time
        }
    }
}