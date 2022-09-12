package com.example.nikestore.data.repo

import com.example.nikestore.data.AddToCartResponse
import com.example.nikestore.data.CartItemCount
import com.example.nikestore.data.CartResponse
import com.example.nikestore.data.MessageResponse
import com.example.nikestore.data.source.CartDataSource
import io.reactivex.Single

class CartRepositoryImpl(private val cartRemoteDataSource: CartDataSource) : CartRepository {
    override fun addToCart(productId: Long): Single<AddToCartResponse> =
        cartRemoteDataSource.addToCart(productId)

    override fun get(): Single<CartResponse> = cartRemoteDataSource.get()

    override fun remove(cartItemId: Long): Single<MessageResponse> =
        cartRemoteDataSource.remove(cartItemId)

    override fun changeCount(cartItemId: Long, count: Int): Single<AddToCartResponse> =
        cartRemoteDataSource.changeCount(cartItemId, count)

    override fun getCartItemCount(): Single<CartItemCount> = cartRemoteDataSource.getCartItemCount()
}