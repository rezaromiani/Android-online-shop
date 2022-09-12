package com.example.nikestore.data.repo.user

import com.example.nikestore.data.TokenContainer
import com.example.nikestore.data.TokenResponse
import com.example.nikestore.data.source.user.UserDateSource
import com.example.nikestore.data.source.user.UserTokenDataSource
import io.reactivex.Completable

class UserRepositoryImpl(
    private val userRemoteDateSource: UserDateSource,
    private val userTokenDataSource: UserTokenDataSource
) : UserRepository {
    override fun login(username: String, password: String): Completable =
        userRemoteDateSource.login(username, password).doOnSuccess {
            onSuccessfulLogin(it, username)
        }.ignoreElement()

    override fun signup( username: String, password: String): Completable {
        return userRemoteDateSource.signup(username, password).flatMap {
            userRemoteDateSource.login(username, password)
        }.doOnSuccess {
            onSuccessfulLogin(it, username)
        }.ignoreElement()
    }

    override fun loadToken() {
        userTokenDataSource.loadToken()
    }

    override fun getUserName(): String = userTokenDataSource.getUserName()

    override fun signOut() {
        userTokenDataSource.signOut()
        TokenContainer.update(null, null)
    }

    private fun onSuccessfulLogin(tokenResponse: TokenResponse, username: String) {
        TokenContainer.update(tokenResponse.access_token, tokenResponse.refresh_token)
        userTokenDataSource.saveToken(tokenResponse.access_token, tokenResponse.refresh_token)
        userTokenDataSource.saveUsername(username)
    }
}