package com.example.nikestore.feature.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.Product
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.view.NikeImageView
import java.lang.IllegalStateException

const val VIEW_TYPE_ROUND = 0
const val VIEW_TYPE_SMALL = 1
const val VIEW_TYPE_LARGE = 2

class ProductListAdapter(
    var viewType: Int = VIEW_TYPE_ROUND,
    val imageLoadingService: ImageLoadingService
) :
    RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {
    var productEventListener: ProductEventListener? = null
    var products: ArrayList<Product> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ProductListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTv: TextView = itemView.findViewById(R.id.productTitleTv)
        private val imageView: NikeImageView = itemView.findViewById(R.id.productIv)
        private val currentPriceTv: TextView = itemView.findViewById(R.id.currentPriceTv)
        private val previousPriceTv: TextView = itemView.findViewById(R.id.previousPriceTv)
        private val favoriteBtn: ImageView = itemView.findViewById(R.id.favoriteBtn)
        fun bind(product: Product) {
            imageLoadingService.load(imageView, product.image)
            titleTv.text = product.title
            currentPriceTv.text = formatPrice(product.price)
            previousPriceTv.text = formatPrice(product.previous_price)
            previousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            if (product.isFavorite)
                favoriteBtn.setImageResource(R.drawable.ic_round_favorite_fill_24)
            else
                favoriteBtn.setImageResource(R.drawable.ic_favorites)
            favoriteBtn.setOnClickListener {
                productEventListener?.onAddtoFavoriteBtnClick(product)
                product.isFavorite = !product.isFavorite
                notifyItemChanged(adapterPosition)
            }
            itemView.setOnClickListener {
                productEventListener?.onProductClick(product)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val layoutResId = when (viewType) {
            VIEW_TYPE_LARGE -> R.layout.item_product_large
            VIEW_TYPE_SMALL -> R.layout.item_product_small
            VIEW_TYPE_ROUND -> R.layout.item_product
            else -> throw IllegalStateException("viewType is not valid")
        }
        return ProductListViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    interface ProductEventListener {
        fun onProductClick(product: Product)
        fun onAddtoFavoriteBtnClick(product: Product)
    }
}