package com.example.firstapp.featureauthen

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//a new file to get the Retrofit object
//a function that will return the Retrofit object.
object ApiClient {
    private const val BASE_URL = "http://hsba-v2.beetechdev.vn:1680/api/v1"

    private val gson = Gson()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .build()

    val chatService: ChatService by lazy {
        retrofit.create(ChatService::class.java)
    }
}