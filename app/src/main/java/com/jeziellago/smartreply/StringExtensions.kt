package com.jeziellago.smartreply

import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage

const val REMOTE_USER_ID = "1"

fun String.toFTMessage(userId: String? = null): FirebaseTextMessage {
    return if (userId.isNullOrEmpty())
        FirebaseTextMessage.createForLocalUser(this, System.currentTimeMillis())
    else
        FirebaseTextMessage.createForRemoteUser(this, System.currentTimeMillis(), userId)
}

fun String.asRemoteUser() = "[Remote User] > $this\n"

fun String.asLocalUser() = "[You] > $this\n"