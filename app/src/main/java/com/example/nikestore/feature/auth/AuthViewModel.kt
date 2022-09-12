package com.example.nikestore.feature.auth

import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.repo.user.UserRepository
import io.reactivex.Completable
import timber.log.Timber

class AuthViewModel(private val userRepository: UserRepository) : NikeViewModel() {
    fun login(username: String, password: String): Completable {
        _progressBarLiveData.postValue(true)
        return userRepository.login(username, password)
            .doFinally { _progressBarLiveData.postValue(false) }
    }

    fun signUp( username: String, password: String): Completable {
        _progressBarLiveData.postValue(true)
        return userRepository.signup( username, password)
            .doOnError {
                Timber.i(it)
            }
            .doFinally { _progressBarLiveData.postValue(false) }
    }
}