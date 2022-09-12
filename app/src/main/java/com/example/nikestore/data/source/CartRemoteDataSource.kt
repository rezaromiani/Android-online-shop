package com.example.nikestore.data.source

import com.example.nikestore.data.AddToCartResponse
import com.example.nikestore.data.CartItemCount
import com.example.nikestore.data.CartResponse
import com.example.nikestore.data.MessageResponse
import com.example.nikestore.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class CartRemoteDataSource(private val apiService: ApiService) : CartDataSource {
    override fun addToCart(productId: Long): Single<AddToCartResponse> = apiService.addToCart(
        JsonObject().apply {
            addProperty("product_id", productId)
        }
    )

    override fun get(): Single<CartResponse> = apiService.getCart()

    override fun remove(cartItemId: Long): Single<MessageResponse> =
        apiService.removeItemFromCart(JsonObject().apply {
            addProperty(
                "cart_item_id",
                cartItemId
            )
        })

    override fun changeCount(cartItemId: Long, count: Int): Single<AddToCartResponse> =
        apiService.changeCount(JsonObject().apply {
            addProperty("cart_item_id", cartItemId)
            addProperty("count", count)
        })

    override fun getCartItemCount(): Single<CartItemCount> = apiService.getCartItemCount()
}