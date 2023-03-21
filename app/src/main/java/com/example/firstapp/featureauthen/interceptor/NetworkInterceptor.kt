package com.example.firstapp.featureauthen.interceptor

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response


class NetworkInterceptor(var mContext: Context) : Interceptor {

    private fun hasConnection(context: Context?): Boolean {
        if (context == null) {
            return false
        }

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        val wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (wifiNetwork != null && wifiNetwork.isConnected) {
            return true
        }

        val mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (mobileNetwork != null && mobileNetwork.isConnected) {
            return true
        }

        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (hasConnection(mContext)) {
            chain.proceed(chain.request())
        } else {
            throw NoConnectivityException()
        }
    }
}

class NoConnectivityException : Exception() {
    override val message: String?
        get() = "Không có kết nối mạng"
}

