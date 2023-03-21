package com.example.firstapp.featureauthen.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetAddress


class NetworkInterceptor(var mContext: Context) : Interceptor {

    private fun hasConnection(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager?.run {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                    result = hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                }
            }
        } else {
            connectivityManager?.run {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                result = networkInfo != null && networkInfo.isConnectedOrConnecting
            }
        }
        if (result) {
            result = try {
                val ipAddr: InetAddress = InetAddress.getByName("google.com")
                !ipAddr.equals("")
            } catch (e: Exception) {
                Log.e("ConnectivityInterceptor", "Error checking internet connection", e)
                false
            }
        }
        return result
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (hasConnection(mContext)) {
            chain.proceed(chain.request())
        } else {
            throw NoConnectivityException()
        }
    }
}

class NoConnectivityException : IOException() {
    override val message: String?
        get() = "Không có kết nối mạng"
}

