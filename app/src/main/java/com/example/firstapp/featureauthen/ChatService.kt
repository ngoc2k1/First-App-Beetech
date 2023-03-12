package com.example.firstapp.featureauthen

import com.example.firstapp.featureauthen.User
import retrofit2.Call
import retrofit2.http.*

//    http://hsba-v2.beetechdev.vn:1680/api/v1/user/login
interface ChatService {
    @FormUrlEncoded
    @POST("/user/login")
    fun postAccount(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("device_token") device_token: String,
        @Field("device") device: Int
        ): Call<User>
}
