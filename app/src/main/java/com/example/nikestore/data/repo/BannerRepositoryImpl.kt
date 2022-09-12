package com.example.nikestore.data.repo

import com.example.nikestore.data.Banner
import com.example.nikestore.data.source.BannerDataSource
import io.reactivex.Single

class BannerRepositoryImpl(private val bannerRemoteDataSource: BannerDataSource) :
    BannerRepository {
    override fun getBanner(): Single<List<Banner>> = bannerRemoteDataSource.getBanner()
}