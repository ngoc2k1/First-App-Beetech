package com.example.firstapp.featureauthen

import com.example.firstapp.featureauthen.entity.LoginRequest
import com.example.firstapp.featureauthen.entity.LoginResponse
import io.reactivex.Observable
import retrofit2.http.*


interface ChatService {
    @POST("user/login")
    fun createAccount(
        @Body loginRequest: LoginRequest,
    ): Observable<LoginResponse?>?
}
