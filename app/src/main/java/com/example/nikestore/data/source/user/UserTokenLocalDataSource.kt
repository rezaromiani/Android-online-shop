package com.example.nikestore.data.source.user

import android.content.SharedPreferences
import com.example.nikestore.data.TokenContainer

class UserTokenLocalDataSource(private val sharedPreferences: SharedPreferences) :
    UserTokenDataSource {
    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN_TOKEN = "refresh_token"
        const val USER_NAME = "username"
    }

    override fun loadToken() {
        TokenContainer.update(
            sharedPreferences.getString(ACCESS_TOKEN, null),
            sharedPreferences.getString(REFRESH_TOKEN_TOKEN, null)
        )
    }

    override fun saveToken(token: String, refreshToken: String) {

        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN, token)
            putString(REFRESH_TOKEN_TOKEN, refreshToken)
        }.apply()
    }

    override fun getUserName(): String =
        sharedPreferences.getString(USER_NAME, "") ?: ""


    override fun saveUsername(username: String) {
        sharedPreferences.edit().apply {
            putString(USER_NAME, username)
        }.apply()
    }

    override fun signOut() {
        sharedPreferences.edit().apply { clear() }.apply()
    }
}