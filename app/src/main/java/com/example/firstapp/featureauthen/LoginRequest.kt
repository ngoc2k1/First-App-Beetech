package com.example.firstapp.featureauthen

data class LoginRequest(
    val username: String,
    val password: String,
    val device_token: String,
    val device: Int
)