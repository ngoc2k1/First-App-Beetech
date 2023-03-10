package com.example.firstapp.featurechat

import android.content.Context
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

fun convertDpToPixel(context: Context, dp: Int): Int {
    val metrics = context.resources.displayMetrics
    val px = dp * (metrics.densityDpi / 160f)
    return px.toInt()
}