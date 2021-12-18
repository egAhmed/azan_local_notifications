package com.ahmed.azanlocalnotifications

import androidx.annotation.Keep

@Keep
enum class RepeatInterval {
    EveryMinute,
    Every5Minute,
    Every10Minute,
    Every15Minute,
    Every20Minute,
    Every30Minute,
    Every40Minute,
    Every50Minute,
    Hourly,
    Hour2,
    Hour4,
    Hour6,
    Hour12,
    Daily,
    Weekly
}