package com.example.firstapp.featureauthen.interceptor

import okhttp3.Interceptor
import okhttp3.Response


object DeviceInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
            .header("device", "2").build();
        return chain.proceed(builder)
    }
}