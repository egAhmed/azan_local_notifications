package com.ahmed.azanlocalnotifications.models

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class MessageDetails(
    var text: String?,
    var timestamp: Long?,
    person: PersonDetails?,
    dataMimeType: String?,
    dataUri: String?
) : Serializable {
    var person: PersonDetails?
    var dataMimeType: String?
    var dataUri: String?

    init {
        this.person = person
        this.dataMimeType = dataMimeType
        this.dataUri = dataUri
    }
}