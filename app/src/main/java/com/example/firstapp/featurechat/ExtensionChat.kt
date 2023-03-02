package com.example.firstapp.featurechat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


fun ViewBinding.layout(context: Context, viewBinding: ViewBinding, parent: ViewGroup) {
//        viewBinding.inflate(
//            LayoutInflater.from(context), parent, false)
}

const val SEND_TEXT = 1
const val RECEIVE_TEXT = 2
const val RECEIVE_PHOTOS = 3
const val SEND_PHOTOS = 4
const val SEND_MULTIPHOTOS = 5

const val REQUEST_GALLERY = 123
const val REQUEST_CAMERA = 124