package com.example.nikestore.feature.shipping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.nikestore.R
import com.example.nikestore.common.*
import com.example.nikestore.data.PurchaseDetail
import com.example.nikestore.data.SubmitOrderResult
import com.example.nikestore.databinding.ActivityShippingBinding
import com.example.nikestore.databinding.ItemPurchaseDetailBinding
import com.example.nikestore.feature.cart.CartItemAdapter
import com.example.nikestore.feature.paymentres.PaymentResActivity
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShippingActivity : NikeActivity() {
    private var _binding: ActivityShippingBinding? = null
    private val binding get() = _binding!!
    private val shippingViewModel: ShippingViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityShippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val purchaseDetail = intent.getParcelableExtra<PurchaseDetail>(EXTRA_KEY_DATA)
            ?: throw IllegalStateException("Purchase Detail cannot be null")



        binding.purchaseDetailView.totalPriceTv.text = formatPrice(purchaseDetail.total_price)
        binding.purchaseDetailView.shippingCostTv.text = formatPrice(purchaseDetail.shipping_cost)
        binding.purchaseDetailView.payablePriceTv.text = formatPrice(purchaseDetail.payable_price)

        binding.shippingToolbar.onBackButtonClickListener = View.OnClickListener { finish() }

        val onClickListener = View.OnClickListener {
            shippingViewModel.submitOrder(
                binding.firstNameEt.text.toString(),
                binding.lastNameEt.text.toString(),
                binding.postalCodeEt.text.toString(),
                binding.phoneNumberEt.text.toString(),
                binding.addressEt.text.toString(),
                if (it.id == R.id.onlinePaymentBtn) PAYMENT_METHOD_ONLINE else PAYMENT_METHOD_COD
            ).asyncNetworkRequest()
                .subscribe(object : NikeSingleObserver<SubmitOrderResult>(compositeDisposable) {
                    override fun onSuccess(t: SubmitOrderResult) {
                        if (t.bank_gateway_url.isNotEmpty()) {
                            openUrlInCustomTab(this@ShippingActivity, t.bank_gateway_url)
                        } else {
                            startActivity(
                                Intent(
                                    this@ShippingActivity,
                                    PaymentResActivity::class.java
                                ).apply {
                                    putExtra(EXTRA_KEY_ID, t.order_id)
                                }
                            )
                        }
                        finish()
                    }

                })
        }

        binding.codBtn.setOnClickListener(onClickListener)
        binding.onlinePaymentBtn.setOnClickListener(onClickListener)
    }
}