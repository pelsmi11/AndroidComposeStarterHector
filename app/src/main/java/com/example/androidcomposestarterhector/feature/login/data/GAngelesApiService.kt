package com.example.androidcomposestarterhector.feature.login.data

import com.example.androidcomposestarterhector.feature.login.data.login.request.LoginApiRequestBody
import com.example.androidcomposestarterhector.feature.login.data.login.response.LoginApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GAngelesApiService {
    @POST("/auth/login")
    suspend fun loginApi(@Body loginApiRequestBody: LoginApiRequestBody): LoginApiResponse;
}