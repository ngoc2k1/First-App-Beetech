package com.example.firstapp.featureauthen

import android.content.Context
import com.example.firstapp.BuildConfig
import com.example.firstapp.featureauthen.interceptor.AuthInterceptor
import com.example.firstapp.featureauthen.interceptor.DeviceInterceptor
import com.example.firstapp.featureauthen.interceptor.NetworkInterceptor
import com.example.firstapp.featureauthen.interceptor.VersionInterceptor
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient(context: Context) {
    private val BASE_URL = "http://hsba-v2.beetechdev.vn:1680/api/v1/"

    private val gson = Gson()

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(NetworkInterceptor(context))
        .addInterceptor(createHttpLoggingInterceptor()) // Log lỗi ra logcat: header lỗi, connect thành công
        .addInterceptor(AuthInterceptor)
        .addInterceptor(VersionInterceptor)
        .addInterceptor(DeviceInterceptor)
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

    private fun createHttpLoggingInterceptor(): Interceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        return httpLoggingInterceptor
    }
}