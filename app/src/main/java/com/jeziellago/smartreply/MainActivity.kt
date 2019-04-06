package com.jeziellago.smartreply

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage
import kotlinx.android.synthetic.main.activity_main.switch_remote as switchRemote
import kotlinx.android.synthetic.main.activity_main.btn_send as btnSend
import kotlinx.android.synthetic.main.activity_main.et_message as inputMessages
import kotlinx.android.synthetic.main.activity_main.txt_smart_reply as txtShowMessages
import kotlinx.android.synthetic.main.activity_main.txt_suggestions as txtShowSuggestions

class MainActivity : AppCompatActivity() {

    private lateinit var smartReply: SmartReply

    private var sendAsRemoteUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
    }

    private fun setupViews() {
        smartReply = SmartReply()
        btnSend.setOnClickListener {
            if (sendAsRemoteUser) sendMessageAsRemoteUser()
            else sendMessageAsLocalUser()
        }
        switchRemote.setOnCheckedChangeListener { _, isChecked ->
            sendAsRemoteUser = isChecked
        }
    }

    private fun sendMessage(message: FirebaseTextMessage) {
        smartReply.addMessage(message)
        smartReply.suggestReplies({
            var suggestions = ""
            it.map { s -> suggestions += "[ $s  ] " }
            txtShowSuggestions.text = suggestions
        }, {
            // No reply
            // ...
        }, {
            // Not supported language
            // ...
        }, {
            // onFailure
            // ...
        })
    }

    private fun sendMessageAsRemoteUser() {
        val message = inputMessages.text.toString().asRemoteUser()
        showMessage(message)
        sendMessage(message.toFTMessage(REMOTE_USER_ID))
    }

    private fun sendMessageAsLocalUser() {
        val message = inputMessages.text.toString().asLocalUser()
        showMessage(message)
        sendMessage(message.toFTMessage())
    }

    private fun showMessage(message: String) {
        val currentText = txtShowMessages.text.toString()
        val newMessages = "$currentText\n$message"

        inputMessages.setText("")
        txtShowSuggestions.text = ""
        txtShowMessages.text = newMessages
    }

}
