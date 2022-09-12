package com.example.nikestore.feature.paymentres

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_ID
import com.example.nikestore.common.formatPrice
import com.example.nikestore.databinding.ActivityPaymentResBinding
import com.example.nikestore.feature.main.MainActivity
import com.example.nikestore.feature.order.OrderHistoryActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaymentResActivity : AppCompatActivity() {
    private val paymentResViewModel: PaymentResViewModel by viewModel {
        val uri: Uri? = intent.data
        if (uri != null)
            parametersOf(uri.getQueryParameter("order_id")!!.toInt())
        else
            parametersOf(intent.extras!!.getInt(EXTRA_KEY_ID))
    }
    private var _binding: ActivityPaymentResBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPaymentResBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentResViewModel.paymentResLiveData.observe(this) {
            binding.orderPriceTv.text = formatPrice(it.payable_price)
            binding.orderStatusTv.text = it.payment_status
            binding.purchaseStatusTv.text =
                if (it.purchase_success) "خرید با موفقیت انجام شد" else "خرید ناموفق بود"
        }

        binding.returnHomeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.orderHistoryBtn.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }
    }
}