package com.jeziellago.smartreply

import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult.*
import java.lang.Exception

internal class SmartReply {

    private val smartReply = FirebaseNaturalLanguage.getInstance().smartReply
    private val conversation = ArrayList<FirebaseTextMessage>()

    fun suggestReplies(onStateSuccess: (suggestions: List<String>) -> Unit,
                       onStateNoReply: () -> Unit,
                       onNotSupportedLanguage: () -> Unit,
                       onFailure: (error: Exception) -> Unit
    ) {

        smartReply.suggestReplies(conversation)
                .addOnSuccessListener { result ->
                    result.apply {
                        when (status) {
                            STATUS_NOT_SUPPORTED_LANGUAGE -> onNotSupportedLanguage()
                            STATUS_SUCCESS -> onStateSuccess(result.suggestions.map { it.text })
                            else -> onStateNoReply()
                        }
                    }
                }
                .addOnFailureListener(onFailure)

    }

    fun addMessage(ftMessage: FirebaseTextMessage) = conversation.add(ftMessage)

    fun clearConversation() = conversation.clear()

}