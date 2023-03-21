package com.example.firstapp.featureauthen.interceptor

import okhttp3.Interceptor
import okhttp3.Response

object VersionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("version", "2.0.0").build();
        return chain.proceed(newRequest)
    }
}