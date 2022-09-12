package com.example.nikestore.data.repo.product

import com.example.nikestore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductRepository {
    fun getProduct(sort: String): Single<List<Product>>

    fun getFavoritProducts(): Single<List<Product>>

    fun addToFavorites(product: Product): Completable

    fun deleteFromFavorites(product: Product): Completable
}