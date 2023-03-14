package com.example.firstapp.featureauthen

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Field as Field


//    http://hsba-v2.beetechdev.vn:1680/api/v1/user/login
interface ChatService {
    @POST("/user/login")
    fun createAccount(
        @Header("version") version: String,
        @Header("device") deviceHeader: Int,
        @Body loginRequest: LoginRequest,
    ): Observable<LoginRequest?>?

//    @POST("/user/login")
//    fun createAccount1(
//        @Field("username") un: String,
//        @Field("password") pw: String,
//        @Field("device_token") devicetoken: String,
//        @Field("device") device: Int,
//        @Header("version") version: String,
//        @Header("device") deviceHeader: Int,
//    ): Observable<LoginRequest?>?
}
