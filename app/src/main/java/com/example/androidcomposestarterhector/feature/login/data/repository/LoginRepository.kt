package com.example.androidcomposestarterhector.feature.login.data.repository

import com.example.androidcomposestarterhector.feature.login.data.GAngelesApiService
import com.example.androidcomposestarterhector.feature.login.data.login.request.LoginApiRequestBody
import com.example.androidcomposestarterhector.feature.login.data.login.response.LoginApiResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiService: GAngelesApiService
) {
    suspend fun login(username: String, password: String): LoginApiResponse {
        // Construyes el request body
        val requestBody = LoginApiRequestBody(
            username = username,
            password = password
        )
        // Llamas al endpoint
        return apiService.loginApi(requestBody)
    }
}