package com.example.nikestore.data.source.user

import com.example.nikestore.data.MessageResponse
import com.example.nikestore.data.TokenResponse
import com.example.nikestore.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class UserRemoteDateSource(private val apiService: ApiService) : UserDateSource {
    companion object {
        const val CLIENT_ID = 2
        const val CLIENT_SECRET = "kyj1c9sVcksqGU4scMX7nLDalkjp2WoqQEf8PKAC"
    }

    override fun login(username: String, password: String): Single<TokenResponse> =
        apiService.getToken(
            JsonObject().apply {
                addProperty("grant_type", "password")
                addProperty("client_id", CLIENT_ID)
                addProperty("client_secret", CLIENT_SECRET)
                addProperty("username", username)
                addProperty("password", password)
            }
        )

    override fun signup(username: String, password: String): Single<MessageResponse> =
        apiService.UserSignUp(JsonObject().apply {
            addProperty("email", username)
            addProperty("password", password)
        })
}