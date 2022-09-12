package com.example.nikestore.feature.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.view.NikeToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderHistoryActivity : NikeActivity() {
    private lateinit var orderHistoryItemAdapter: OrderHistoryItemAdapter
    private val viewModel: OrderHistoryViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        orderHistoryItemAdapter = OrderHistoryItemAdapter(this)
        findViewById<RecyclerView>(R.id.orderHistoryRV).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = orderHistoryItemAdapter
        }
        viewModel.orderLiveData.observe(this) {
            orderHistoryItemAdapter.orderHistoryItems = it
        }

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }
        findViewById<NikeToolbar>(R.id.orderHistoryToolbar).onBackButtonClickListener =
            View.OnClickListener {
                finish()
            }
    }
}