package com.example.nikestore.feature.cart

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.CartItem
import com.example.nikestore.data.PurchaseDetail
import com.example.nikestore.databinding.ItemCartBinding
import com.example.nikestore.databinding.ItemPurchaseDetailBinding
import com.example.nikestore.services.ImageLoadingService

class CartItemAdapter(
    val imageLoadingService: ImageLoadingService,
    val cartItemViewCallBack: CartItemViewCallBack,
    private val cartItems: MutableList<CartItem>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var purchaseDetail: PurchaseDetail? = null
        set(value) {
            field = value
            notifyItemChanged(cartItems.size )
        }

    companion object {
        const val VIEW_TYPE_CART_ITEM = 0
        const val VIEW_TYPE_PURCHASE_DETAIL = 1
    }

    inner class CartItemViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.productTitleTv.text = cartItem.product.title
            binding.cartItemCountTv.text = cartItem.count.toString()
            binding.previousPriceTv.text = formatPrice(cartItem.product.price + cartItem.product.discount)
            binding.previousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.priceTv.text = formatPrice(cartItem.product.price)
            imageLoadingService.load(binding.productIv, cartItem.product.image)

            binding.changeCountProgressBar.visibility =
                if (cartItem.changeCountProgressBarIsVisible) View.VISIBLE else View.GONE
            binding.cartItemCountTv.visibility =
                if (cartItem.changeCountProgressBarIsVisible) View.INVISIBLE else View.VISIBLE

            setClickListeners(cartItem)
        }

        private fun setClickListeners(cartItem: CartItem) {
            binding.removeFromCart.setOnClickListener {
                cartItemViewCallBack.onRemoveCartItemButtonOnClick(cartItem)
            }
            binding.productIv.setOnClickListener {
                cartItemViewCallBack.onProductImageClick(cartItem)
            }
            binding.increaseBtn.setOnClickListener {
                cartItem.changeCountProgressBarIsVisible = true
                binding.changeCountProgressBar.visibility = View.VISIBLE
                binding.cartItemCountTv.visibility = View.INVISIBLE
                cartItemViewCallBack.onIncreaseCartItemButtonClick(cartItem)
            }
            binding.decreaseBtn.setOnClickListener {
                if (cartItem.count > 1) {
                    cartItem.changeCountProgressBarIsVisible = true
                    binding.changeCountProgressBar.visibility = View.VISIBLE
                    binding.cartItemCountTv.visibility = View.INVISIBLE
                    cartItemViewCallBack.onDecreaseCartItemClick(cartItem)
                }
            }
        }
    }

     class PurchaceDetailViewHolder(private val binding: ItemPurchaseDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            total_price: Long,
            payable_price: Long,
            shipping_cost: Long,
        ) {
            binding.payablePriceTv.text = formatPrice(payable_price)
            binding.totalPriceTv.text = formatPrice(total_price)
            binding.shippingCostTv.text = formatPrice(shipping_cost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_CART_ITEM) {
            return CartItemViewHolder(
                ItemCartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else
            return PurchaceDetailViewHolder(
                ItemPurchaseDetailBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == cartItems.size)
            VIEW_TYPE_PURCHASE_DETAIL
        else
            VIEW_TYPE_CART_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartItemViewHolder)
            holder.bind(cartItems[position])
        else
            purchaseDetail?.let {
                (holder as PurchaceDetailViewHolder).bind(
                    it.total_price, it.payable_price, it.shipping_cost
                )
            }
    }

    override fun getItemCount(): Int = cartItems.size + 1

    fun removeCartItem(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems.remove(cartItem)
            notifyItemRemoved(index)
        }
    }

    fun changeCount(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems[index].changeCountProgressBarIsVisible = false
            notifyItemChanged(index)
        }
    }

    interface CartItemViewCallBack {
        fun onRemoveCartItemButtonOnClick(cartItem: CartItem)
        fun onIncreaseCartItemButtonClick(cartItem: CartItem)
        fun onDecreaseCartItemClick(cartItem: CartItem)
        fun onProductImageClick(cartItem: CartItem)
    }
}