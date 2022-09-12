package com.example.nikestore.data.source.user

import com.example.nikestore.data.MessageResponse
import com.example.nikestore.data.TokenResponse
import io.reactivex.Single

interface UserDateSource {
    fun login(username: String, password: String): Single<TokenResponse>
    fun signup( username: String, password: String): Single<MessageResponse>
}