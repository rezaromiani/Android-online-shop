package com.example.nikestore.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.NikeCompletable
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.Banner
import com.example.nikestore.data.Product
import com.example.nikestore.data.SORT_LATEST
import com.example.nikestore.data.SORT_POPULAR
import com.example.nikestore.data.repo.BannerRepository
import com.example.nikestore.data.repo.product.ProductRepository
import io.reactivex.Completable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class HomeViewModel(
    private val productRepository: ProductRepository,
    bannerRepository: BannerRepository
) :
    NikeViewModel() {
    private val _productListLiveData = MutableLiveData<List<Product>>()
    val productListLiveData: LiveData<List<Product>>
        get() = _productListLiveData

    private val _popularProductListLiveData = MutableLiveData<List<Product>>()
    val popularProductListLiveData: LiveData<List<Product>>
        get() = _popularProductListLiveData

    private val _bannerListLiveData = MutableLiveData<List<Banner>>()
    val bannerListLiveData: LiveData<List<Banner>>
        get() = _bannerListLiveData

    init {
        _progressBarLiveData.value = true
        productRepository.getProduct(SORT_LATEST.toString()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { _progressBarLiveData.postValue(false) }
            .subscribe(object : SingleObserver<List<Product>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Product>) {
                    _productListLiveData.value = t
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                }

            })
        productRepository.getProduct(SORT_POPULAR.toString())
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<Product>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Product>) {
                    _popularProductListLiveData.postValue(t)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                }

            });

        bannerRepository.getBanner().subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<Banner>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Banner>) {
                    _bannerListLiveData.postValue(t)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                }

            })
    }

    fun addToFavorite(product: Product) {
        if (product.isFavorite)
            productRepository.deleteFromFavorites(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletable(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = false
                    }
                })
        else
            productRepository.addToFavorites(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletable(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = true
                    }
                })
    }

}