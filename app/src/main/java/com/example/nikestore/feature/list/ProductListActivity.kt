package com.example.nikestore.feature.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.data.Product
import com.example.nikestore.databinding.ActivityProductListBinding
import com.example.nikestore.feature.detail.ProductDetailActivity
import com.example.nikestore.feature.home.ProductListAdapter
import com.example.nikestore.feature.home.VIEW_TYPE_LARGE
import com.example.nikestore.feature.home.VIEW_TYPE_SMALL
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductListActivity : NikeActivity(), ProductListAdapter.ProductEventListener {
    private lateinit var binding: ActivityProductListBinding
    private val productListViewModel: ProductListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_DATA
            )
        )
    }
    private val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_SMALL) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productListAdapter.productEventListener = this

        val gridLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        binding.productsRv.apply {
            layoutManager = gridLayoutManager
            adapter = productListAdapter
        }

        productListViewModel.productListLiveData.observe(this) {
            productListAdapter.products = it as ArrayList<Product>
        }

        productListViewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        binding.viewTypeChangeBtn.setOnClickListener {
            if (productListAdapter.viewType == VIEW_TYPE_SMALL) {
                productListAdapter.viewType = VIEW_TYPE_LARGE
                binding.viewTypeChangeBtn.setImageResource(R.drawable.ic_view_type_large)
                gridLayoutManager.spanCount = 1
                productListAdapter.notifyDataSetChanged()
            } else {
                productListAdapter.viewType = VIEW_TYPE_SMALL
                gridLayoutManager.spanCount = 2
                binding.viewTypeChangeBtn.setImageResource(R.drawable.ic_grid)
                productListAdapter.notifyDataSetChanged()
            }
        }

        productListViewModel.selectedSortLiveData.observe(this) {
            binding.sortTitle.text = getString(it)
        }

        binding.sortBtn.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
                .setSingleChoiceItems(
                    R.array.sortTitlesArray,
                    productListViewModel.sort
                ) { dialog, selectedIndex ->
                    dialog.dismiss()
                    productListViewModel.onSelectedSortChangedByUser(selectedIndex)
                }.setTitle(getString(R.string.sort))

            dialog.show()
        }

        binding.productListToolbar.onBackButtonClickListener = View.OnClickListener {
            finish()
        }
    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onAddtoFavoriteBtnClick(product: Product) {
        productListViewModel.addToFavorite(product)
    }
}