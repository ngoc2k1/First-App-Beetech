package com.example.firstapp.featurechat

import android.net.Uri
import android.view.textclassifier.ConversationActions.Message

data class Chat(val isSend: Int, val message: String, val isSendPhoto: Int, val uri:Uri?=null)