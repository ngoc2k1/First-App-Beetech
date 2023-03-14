package com.example.firstapp.featureauthen

import com.google.gson.annotations.SerializedName

class RequestError {
    @SerializedName("code")
    var errorCode: Int? = null
    @SerializedName("msg")
    var errorMessage: String? = null
}