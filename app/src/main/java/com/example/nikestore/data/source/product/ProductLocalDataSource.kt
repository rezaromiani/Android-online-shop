package com.example.nikestore.data.source.product

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.nikestore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ProductLocalDataSource : ProductDataSource {
    override fun getProduct(sort: String): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    @Query("SELECT * FROM tbl_favorite")
    override fun getFavoritProducts(): Single<List<Product>>

    @Insert(onConflict = REPLACE)
    override fun addToFavorites(product: Product): Completable

    @Delete
    override fun deleteFromFavorites(product: Product): Completable
}