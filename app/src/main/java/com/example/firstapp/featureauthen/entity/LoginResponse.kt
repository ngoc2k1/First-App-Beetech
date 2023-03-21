package com.example.firstapp.featureauthen.entity


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: DataChatResponse,
    @SerializedName("msg")
    var msg: String
)