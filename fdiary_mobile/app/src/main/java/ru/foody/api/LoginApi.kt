package ru.foody.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.foody.dto.auth.AuthRequest
import ru.foody.dto.auth.AuthResponse
import ru.foody.dto.auth.RegistrationRequest

interface LoginApi {

    @POST("/auth/register")
    fun register(@Body authRequest: RegistrationRequest) : Call<Any>

    @POST("/auth/login")
    fun login(@Body loginRequest: AuthRequest) : Call<AuthResponse>

    @GET("/auth/token")
    fun token() : Call<AuthResponse>
}
