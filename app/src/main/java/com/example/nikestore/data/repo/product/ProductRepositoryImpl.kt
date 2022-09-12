package com.example.nikestore.data.repo.product

import com.example.nikestore.data.Product
import com.example.nikestore.data.source.product.ProductDataSource
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImpl(
    private val remoteDataSource: ProductDataSource,
    private val productLocalDataSource: ProductDataSource
) : ProductRepository {
    override fun getProduct(sort: String): Single<List<Product>> =
        productLocalDataSource.getFavoritProducts()
            .flatMap { favoriteProducts ->
                remoteDataSource.getProduct(sort).doOnSuccess {
                    val favIds = favoriteProducts.map {
                        it.id
                    }
                    it.forEach { product ->
                        if (favIds.contains(product.id))
                            product.isFavorite = true
                    }
                }
            }

    override fun getFavoritProducts(): Single<List<Product>> =
        productLocalDataSource.getFavoritProducts()

    override fun addToFavorites(product: Product): Completable =
        productLocalDataSource.addToFavorites(product)

    override fun deleteFromFavorites(product: Product): Completable =
        productLocalDataSource.deleteFromFavorites(product)
}