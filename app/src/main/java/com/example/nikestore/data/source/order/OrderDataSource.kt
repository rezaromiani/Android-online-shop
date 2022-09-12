package com.example.nikestore.data.source.order

import com.example.nikestore.data.OrderHistoryItem
import com.example.nikestore.data.PaymentRes
import com.example.nikestore.data.SubmitOrderResult
import io.reactivex.Single

interface OrderDataSource {
    fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult>

    fun payemntRes(OrderId: Int): Single<PaymentRes>

    fun getOrders():Single<List<OrderHistoryItem>>
}