package com.example.nikestore.feature.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.R
import com.example.nikestore.common.NikeCompletable
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.product.ProductRepository
import io.reactivex.schedulers.Schedulers

class ProductListViewModel(
    private val productRepository: ProductRepository,
    var sort: Int
) : NikeViewModel() {

    private val _productListLiveData = MutableLiveData<List<Product>>()
    val productListLiveData: LiveData<List<Product>>
        get() = _productListLiveData

    private val _selectedSortLiveData: MutableLiveData<Int> = MutableLiveData()
    val selectedSortLiveData: LiveData<Int>
        get() = _selectedSortLiveData
    private val sortTitle = arrayOf(
        R.string.sortLatest,
        R.string.sortPopular,
        R.string.sortPriceHighToLow,
        R.string.sortPriceLowToHigh
    )

    init {
        getProducts()
        _selectedSortLiveData.value = sortTitle[sort]
    }

    fun getProducts() {
        _progressBarLiveData.postValue(true)
        productRepository.getProduct(sort.toString())
            .subscribeOn(Schedulers.io())
            .doFinally { _progressBarLiveData.postValue(false) }
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    _productListLiveData.postValue(t)
                }
            })
    }

    fun onSelectedSortChangedByUser(sort: Int) {
        this.sort = sort
        _selectedSortLiveData.value = sortTitle[sort]
        getProducts()
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