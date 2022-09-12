package com.example.nikestore.feature.detail

import android.os.Bundle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.Comment
import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.CartRepository
import com.example.nikestore.data.repo.comment.CommentRepository
import io.reactivex.Completable
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ProductDetailViewModel(
    bundle: Bundle,
    commentRepository: CommentRepository,
    private val cartRepository: CartRepository
) :
    NikeViewModel() {

    private val _productLiveData: MutableLiveData<Product> = MutableLiveData()
    val productLiveData: LiveData<Product>
        get() = _productLiveData

    private val _commentLiveData: MutableLiveData<List<Comment>> = MutableLiveData()
    val commentLiveData: LiveData<List<Comment>>
        get() = _commentLiveData

    init {
        val data: Product? = bundle.getParcelable(EXTRA_KEY_DATA)
        if (data is Product)
            _productLiveData.value = data
        else throw IllegalStateException("data must be instance of Product!")
        _progressBarLiveData.postValue(true)
        commentRepository.getAll(productId = _productLiveData.value!!.id, page = 1)
            .subscribeOn(Schedulers.io())
            .doFinally { _progressBarLiveData.postValue(false) }
            .subscribe(object : SingleObserver<List<Comment>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Comment>) {
                    _commentLiveData.postValue(t)
                }

                override fun onError(e: Throwable) {
                    TODO("Not yet implemented")
                }
            })

    }

    fun onAddToCartBtn(): Completable =
        cartRepository.addToCart(_productLiveData.value!!.id).ignoreElement()

}