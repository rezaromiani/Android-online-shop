package com.example.nikestore.feature.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.R
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.*
import com.example.nikestore.data.repo.CartRepository
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class CartViewModel(private val cartRepository: CartRepository) : NikeViewModel() {

    private val _cartItemLiveData = MutableLiveData<List<CartItem>>()
    val cartItemLiveData: LiveData<List<CartItem>>
        get() = _cartItemLiveData

    private val _purchaseDetailLiveData = MutableLiveData<PurchaseDetail>()
    val purchaseDetailLiveData: LiveData<PurchaseDetail>
        get() = _purchaseDetailLiveData

    private val _emptyStateLiveData = MutableLiveData<EmptyState>()
    val emptyStateLiveData: LiveData<EmptyState>
        get() = _emptyStateLiveData

    fun getCartItem() {
        _progressBarLiveData.postValue(true)

        if (!TokenContainer.token.isNullOrEmpty()) {
            _emptyStateLiveData.postValue(EmptyState(false))
            cartRepository.get().subscribeOn(Schedulers.io())
                .doFinally { _progressBarLiveData.postValue(false) }
                .subscribe(object : NikeSingleObserver<CartResponse>(compositeDisposable) {
                    override fun onSuccess(t: CartResponse) {
                        if (t.cart_items.isNotEmpty()) {
                            _cartItemLiveData.postValue(t.cart_items)
                            _emptyStateLiveData.postValue(EmptyState(false))
                            _purchaseDetailLiveData.postValue(
                                PurchaseDetail(
                                    t.total_price,
                                    t.payable_price,
                                    t.shipping_cost
                                )
                            )
                        } else
                            _emptyStateLiveData.postValue(EmptyState(true, R.string.cartEmptyState))


                    }

                })
        } else
            _emptyStateLiveData.postValue(
                EmptyState(
                    true,
                    R.string.cartEmptyStateLoginRequired,
                    true
                )
            )

    }

    fun removeItemFromCart(cartItem: CartItem): Completable {
        return cartRepository.remove(cartItemId = cartItem.cart_item_id)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count -= cartItem.count
                    EventBus.getDefault().postSticky(it)
                }
                cartItemLiveData.value?.let {
                    if (it.isEmpty())
                        _emptyStateLiveData.postValue(EmptyState(true, R.string.cartEmptyState))
                }
            }
            .ignoreElement()
    }

    fun increaseCartItemCount(cartItem: CartItem): Completable {
        return cartRepository.changeCount(
            cartItemId = cartItem.cart_item_id,
            count = ++cartItem.count
        )
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count += 1
                    EventBus.getDefault().postSticky(it)
                }
            }
            .ignoreElement()
    }

    fun decreaseCartItemCount(cartItem: CartItem): Completable {
        return cartRepository.changeCount(
            cartItemId = cartItem.cart_item_id,
            count = --cartItem.count
        )
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count -= 1
                    EventBus.getDefault().postSticky(it)
                }
            }
            .ignoreElement()
    }

    fun refresh() {
        getCartItem()
    }

    private fun calculateAndPublishPurchaseDetail() {
        _cartItemLiveData.value?.let { cartItems ->
            _purchaseDetailLiveData.value?.let { purchaseDetail ->
                var totalPrice = 0L
                var payablePrice = 0L
                cartItems.forEach {
                    totalPrice += (it.product.price * it.count).toInt()
                    payablePrice += (it.product.price.toInt() - it.product.discount) * it.count
                }
                purchaseDetail.total_price = totalPrice
                purchaseDetail.payable_price = payablePrice
                _purchaseDetailLiveData.postValue(purchaseDetail)
            }
        }
    }


}