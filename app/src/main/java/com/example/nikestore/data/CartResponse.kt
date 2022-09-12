package com.example.nikestore.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CartResponse(
    val cart_items: List<CartItem>,
    val payable_price: Long,
    val shipping_cost: Long,
    val total_price: Long
)
@Parcelize
data class PurchaseDetail(
    var total_price: Long,
    var payable_price: Long,
    val shipping_cost: Long,
) : Parcelable