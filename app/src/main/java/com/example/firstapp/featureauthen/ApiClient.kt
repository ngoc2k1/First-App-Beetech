package com.example.firstapp.featureauthen

import android.content.Context
import com.example.firstapp.featureauthen.interceptor.AuthInterceptor
import com.example.firstapp.featureauthen.interceptor.DeviceInterceptor
import com.example.firstapp.featureauthen.interceptor.NetworkInterceptor
import com.example.firstapp.featureauthen.interceptor.VersionInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient (context:Context){
    private val BASE_URL = "http://hsba-v2.beetechdev.vn:1680/api/v1/"

    private val gson = Gson()

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor)
        .addInterceptor(VersionInterceptor)
        .addInterceptor(DeviceInterceptor)
        .addNetworkInterceptor(NetworkInterceptor(context))
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val chatService: ChatService by lazy {
        retrofit.create(ChatService::class.java)
    }
}