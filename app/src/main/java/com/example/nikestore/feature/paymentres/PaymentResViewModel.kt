package com.example.nikestore.feature.paymentres

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.PaymentRes
import com.example.nikestore.data.repo.order.OrderRepository

class PaymentResViewModel(orderId: Int, private val orderRepository: OrderRepository) :
    NikeViewModel() {
    private val _paymentResLiveData = MutableLiveData<PaymentRes>()
    val paymentResLiveData: LiveData<PaymentRes>
        get() = _paymentResLiveData

    init {

        orderRepository.payemntRes(orderId).asyncNetworkRequest()
            .subscribe(object : NikeSingleObserver<PaymentRes>(compositeDisposable) {
                override fun onSuccess(t: PaymentRes) {
                    _paymentResLiveData.postValue(t)
                }


            })
    }
}