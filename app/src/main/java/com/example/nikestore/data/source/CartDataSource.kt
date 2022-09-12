package com.example.nikestore.data.source

import com.example.nikestore.data.AddToCartResponse
import com.example.nikestore.data.CartItemCount
import com.example.nikestore.data.CartResponse
import com.example.nikestore.data.MessageResponse
import io.reactivex.Single

interface CartDataSource {
    fun addToCart(productId: Long): Single<AddToCartResponse>

    fun get(): Single<CartResponse>

    fun remove(cartItemId: Long): Single<MessageResponse>

    fun changeCount(cartItemId: Long, count: Int): Single<AddToCartResponse>

    fun getCartItemCount(): Single<CartItemCount>
}