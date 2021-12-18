package com.ahmed.azanlocalnotifications.models.styles

import androidx.annotation.Keep
import com.dexterous.flutterlocalnotifications.models.MessageDetails
import com.dexterous.flutterlocalnotifications.models.PersonDetails
import java.util.ArrayList

@Keep
class MessagingStyleInformation(
    person: PersonDetails,
    conversationTitle: String,
    groupConversation: Boolean,
    messages: ArrayList<MessageDetails?>,
    htmlFormatTitle: Boolean?,
    htmlFormatBody: Boolean?
) : DefaultStyleInformation(htmlFormatTitle, htmlFormatBody) {
    var person: PersonDetails
    var conversationTitle: String
    var groupConversation: Boolean
    var messages: ArrayList<MessageDetails>

    init {
        this.person = person
        this.conversationTitle = conversationTitle
        this.groupConversation = groupConversation
        this.messages = messages
    }
}