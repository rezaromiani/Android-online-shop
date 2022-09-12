package com.example.nikestore.feature.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.OrderHistoryItem
import com.example.nikestore.data.repo.order.OrderRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OrderHistoryViewModel(private val orderRepository: OrderRepository) : NikeViewModel() {

    private val _ordersLiveData = MutableLiveData<List<OrderHistoryItem>>()
    val orderLiveData: LiveData<List<OrderHistoryItem>>
        get() = _ordersLiveData

    init {
        _progressBarLiveData.postValue(true)
        orderRepository.getOrders().asyncNetworkRequest()
            .doFinally { _progressBarLiveData.postValue(false) }
            .subscribe(object : NikeSingleObserver<List<OrderHistoryItem>>(compositeDisposable) {
                override fun onSuccess(t: List<OrderHistoryItem>) {
                    _ordersLiveData.postValue(t)
                }

            })
    }
}