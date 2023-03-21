package com.example.firstapp.featureauthen

import com.example.firstapp.featureauthen.entity.LoginRequest
import com.example.firstapp.featureauthen.entity.LoginResponse
import io.reactivex.Observable
import retrofit2.http.*


//    http://hsba-v2.beetechdev.vn:1680/api/v1/user/login
interface ChatService {
    @POST("user/login")
    fun createAccount(
//        @Header("version") version: String,
//        @Header("device") deviceHeader: Int,
        @Body loginRequest: LoginRequest,
    ): Observable<LoginResponse?>?
}
