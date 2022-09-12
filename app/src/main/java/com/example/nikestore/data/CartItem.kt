package com.example.nikestore.data

data class CartItem(
    val cart_item_id: Long,
    var count: Int,
    val product: Product,
    var changeCountProgressBarIsVisible: Boolean = false
)