package com.example.nikestore.data.source.user

interface UserTokenDataSource {
    fun loadToken()
    fun saveToken(token: String, refreshToken: String)
    fun getUserName():String
    fun saveUsername(username: String)
    fun signOut()
}