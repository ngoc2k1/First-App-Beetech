package com.example.firstapp.featureauthen.interceptor

import com.example.firstapp.featureauthen.HawkKey
import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.Response


object AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = Hawk.get(HawkKey.ACCESS_TOKEN, "nothing")

        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}