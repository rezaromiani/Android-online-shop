package com.example.nikestore.data.repo.user

import io.reactivex.Completable

interface UserRepository {
    fun login(username: String, password: String): Completable
    fun signup( username: String, password: String): Completable
    fun loadToken()
    fun getUserName():String
    fun signOut()
}