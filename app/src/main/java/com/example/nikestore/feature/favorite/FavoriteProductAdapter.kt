package com.example.nikestore.feature.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.data.Product
import com.example.nikestore.databinding.ItemFavoriteProductBinding
import com.example.nikestore.services.ImageLoadingService

class FavoriteProductAdapter(
    private val imageLoadingService: ImageLoadingService,
    private val eventListener: FavoriteProductEventListener
) :
    RecyclerView.Adapter<FavoriteProductAdapter.FravoritProductViewHolder>() {
    var products: MutableList<Product> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class FravoritProductViewHolder(private val binding: ItemFavoriteProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productTitleTv.text = product.title
            imageLoadingService.load(binding.productImage, product.image)
            binding.root.setOnClickListener {
                eventListener.onCLick(product)
            }
            binding.root.setOnLongClickListener {
                eventListener.onLongClick(product)
                return@setOnLongClickListener false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FravoritProductViewHolder {
        return FravoritProductViewHolder(
            ItemFavoriteProductBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FravoritProductViewHolder, position: Int) {
        holder.bind(product = products[position])
    }

    override fun getItemCount(): Int = products.size

    fun removeItem(product: Product) {
        val index = products.indexOf(product)
        if (index > -1) {
            products.remove(product)
            notifyItemRemoved(index)
        }
    }

    interface FavoriteProductEventListener {
        fun onCLick(product: Product)
        fun onLongClick(product: Product)
    }
}