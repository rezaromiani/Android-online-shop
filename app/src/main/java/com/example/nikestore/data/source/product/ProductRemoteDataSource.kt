package com.example.nikestore.data.source.product

import com.example.nikestore.data.Product
import com.example.nikestore.services.http.ApiService
import io.reactivex.Completable
import io.reactivex.Single

class ProductRemoteDataSource(private val apiService: ApiService) : ProductDataSource {
    override fun getProduct(sort: String): Single<List<Product>> = apiService.getProduct(sort)

    override fun getFavoritProducts(): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }
}