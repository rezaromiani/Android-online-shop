package com.example.nikestore.data.source

import com.example.nikestore.data.Banner
import com.example.nikestore.services.http.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(private val apiService: ApiService) : BannerDataSource {
    override fun getBanner(): Single<List<Banner>> = apiService.getBanner()
}