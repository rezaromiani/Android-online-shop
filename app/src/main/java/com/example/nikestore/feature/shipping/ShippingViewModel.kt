package com.example.nikestore.feature.shipping

import androidx.lifecycle.ViewModel
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.SubmitOrderResult
import com.example.nikestore.data.repo.order.OrderRepository
import io.reactivex.Single

const val PAYMENT_METHOD_COD = "cash_on_delivery"
const val PAYMENT_METHOD_ONLINE = "online"
class ShippingViewModel(private val orderRepository: OrderRepository) : NikeViewModel() {
    fun submitOrder(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> {
        return orderRepository.submit(
            firstName,
            lastName,
            postalCode,
            phoneNumber,
            address,
            paymentMethod
        )
    }
}