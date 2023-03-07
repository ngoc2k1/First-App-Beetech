package com.example.firstapp.featurechat.photo

import android.net.Uri

data class Photo(
    val uri: Uri,
    var isClicked: Boolean = false
)