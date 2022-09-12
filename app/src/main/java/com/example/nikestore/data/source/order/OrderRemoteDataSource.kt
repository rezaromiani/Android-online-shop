package com.example.nikestore.data.source.order

import com.example.nikestore.data.OrderHistoryItem
import com.example.nikestore.data.PaymentRes
import com.example.nikestore.data.SubmitOrderResult
import com.example.nikestore.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class OrderRemoteDataSource(private val apiService: ApiService) : OrderDataSource {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> = apiService.submitOreder(JsonObject().apply {
        addProperty("first_name", firstName)
        addProperty("last_name", lastName)
        addProperty("postal_code", postalCode)
        addProperty("mobile", phoneNumber)
        addProperty("address", address)
        addProperty("payment_method", paymentMethod)
    })

    override fun payemntRes(orderId: Int): Single<PaymentRes> =
        apiService.paymentRes(orderId = orderId)

    override fun getOrders(): Single<List<OrderHistoryItem>> = apiService.getOrders()
}