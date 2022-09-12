package com.example.nikestore.feature.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.*
import com.example.nikestore.data.CartItem
import com.example.nikestore.databinding.FragmentCartBinding
import com.example.nikestore.feature.auth.AuthActivity
import com.example.nikestore.feature.detail.ProductDetailActivity
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.feature.shipping.ShippingActivity
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : NikeFragment(), CartItemAdapter.CartItemViewCallBack {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by viewModel()
    private var cartItemAdapter: CartItemAdapter? = null
    val imageLoadingService: ImageLoadingService by inject()
    private val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        cartViewModel.cartItemLiveData.observe(viewLifecycleOwner) {
            binding.cartItemRv.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                cartItemAdapter = CartItemAdapter(
                    imageLoadingService,
                    this@CartFragment,
                    it as MutableList<CartItem>
                )
                adapter = cartItemAdapter
            }
        }

        cartViewModel.purchaseDetailLiveData.observe(viewLifecycleOwner) { purchaseDetail ->
            cartItemAdapter?.let {
                it.purchaseDetail = purchaseDetail
            }
        }

        cartViewModel.emptyStateLiveData.observe(viewLifecycleOwner) { emptystate ->
            if (emptystate.mustShow) {
                val emptySateView = showEmptyState(R.layout.view_cart_empty_state)

                emptySateView?.let {
                    emptySateView.findViewById<TextView>(R.id.emptyStateMessage).text =
                        getString(emptystate.messageResId)
                    emptySateView.findViewById<View>(R.id.emptyStateCartBtn).visibility =
                        if (emptystate.mustShowToActionButton) View.VISIBLE else View.GONE
                    emptySateView.findViewById<View>(R.id.emptyStateCartBtn).setOnClickListener {
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                    }
                }
            } else
                binding.root.findViewById<View>(R.id.emptyStateRootView)?.visibility = View.GONE
        }

        binding.payBtn.setOnClickListener {
            startActivity(Intent(requireContext(),ShippingActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA,cartViewModel.purchaseDetailLiveData.value)
            })
        }
    }

    override fun onStart() {
        super.onStart()
        cartViewModel.refresh()
    }

    override fun onRemoveCartItemButtonOnClick(cartItem: CartItem) {
        cartViewModel.removeItemFromCart(cartItem).asyncNetworkRequest()
            .subscribe(object : NikeCompletable(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.removeCartItem(cartItem)
                }
            })
    }

    override fun onIncreaseCartItemButtonClick(cartItem: CartItem) {
        cartViewModel.increaseCartItemCount(cartItem).asyncNetworkRequest()
            .subscribe(object : NikeCompletable(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.changeCount(cartItem)
                }
            })
    }

    override fun onDecreaseCartItemClick(cartItem: CartItem) {
        cartViewModel.decreaseCartItemCount(cartItem).asyncNetworkRequest()
            .subscribe(object : NikeCompletable(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.changeCount(cartItem)
                }
            })
    }

    override fun onProductImageClick(cartItem: CartItem) {
        startActivity(Intent(requireActivity(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, cartItem.product)
        })
    }
}