package com.example.nikestore.data.repo.order

import com.example.nikestore.data.OrderHistoryItem
import com.example.nikestore.data.PaymentRes
import com.example.nikestore.data.SubmitOrderResult
import com.example.nikestore.data.source.order.OrderDataSource
import io.reactivex.Single

class OrderRepositoryImpl(private val orderRemoteDataSource: OrderDataSource) : OrderRepository {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> = orderRemoteDataSource.submit(
        firstName,
        lastName,
        postalCode,
        phoneNumber,
        address,
        paymentMethod
    )

    override fun payemntRes(orderId: Int): Single<PaymentRes> =
        orderRemoteDataSource.payemntRes(orderId)

    override fun getOrders(): Single<List<OrderHistoryItem>> = orderRemoteDataSource.getOrders()
}