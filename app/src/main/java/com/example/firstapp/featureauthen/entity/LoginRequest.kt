package com.example.firstapp.featureauthen.entity

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("device_token")
    var deviceToken: String,
    @SerializedName("device")
    var device: Int
)