package com.example.nikestore.feature.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.convertDpToPixel
import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.OrderHistoryItem
import com.example.nikestore.view.NikeImageView

class OrderHistoryItemAdapter(val context: Context) :
    RecyclerView.Adapter<OrderHistoryItemAdapter.ViewHolder>() {
     var orderHistoryItems: List<OrderHistoryItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    val layoutParams: LinearLayout.LayoutParams

    init {
        val size = convertDpToPixel(100f, context).toInt()
        val margine = convertDpToPixel(8f, context).toInt()
        layoutParams = LinearLayout.LayoutParams(size, size)
        layoutParams.setMargins(margine, 0, margine, 0)
    }


    inner class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderIdTv = itemView.findViewById<TextView>(R.id.orderIdTv)
        val orderPriceTv = itemView.findViewById<TextView>(R.id.orderPriceTV)
        val orderproductList = itemView.findViewById<LinearLayout>(R.id.orderProductList)
        fun bind(orderHistoryItem: OrderHistoryItem) {
            orderIdTv.text = orderHistoryItem.id.toString()
            orderPriceTv.text = formatPrice(orderHistoryItem.payable)
            orderproductList.removeAllViews()
            orderHistoryItem.order_items.forEach { orderItem ->
                val imageView = NikeImageView(context)
                imageView.layoutParams = layoutParams
                imageView.setImageURI(orderItem.product.image)
                orderproductList.addView(imageView)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_history, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderHistoryItems[position])
    }


    override fun getItemCount(): Int = orderHistoryItems.size
}