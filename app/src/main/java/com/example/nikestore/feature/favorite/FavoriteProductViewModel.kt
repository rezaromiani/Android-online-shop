package com.example.nikestore.feature.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.NikeCompletable
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.product.ProductRepository
import io.reactivex.Completable
import timber.log.Timber

class FavoriteProductViewModel(private val productRepository: ProductRepository) : NikeViewModel() {
    private val _productLiveData: MutableLiveData<List<Product>> = MutableLiveData()
    val productLiveData: LiveData<List<Product>>
        get() = _productLiveData

    init {
        _progressBarLiveData.postValue(true)
        productRepository.getFavoritProducts().asyncNetworkRequest()
            .doFinally { _progressBarLiveData.postValue(false) }
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    _productLiveData.postValue(t)
                }

            })
    }

    fun removeFromFavorite(product: Product): Completable {
        return productRepository.deleteFromFavorites(product)
    }
}