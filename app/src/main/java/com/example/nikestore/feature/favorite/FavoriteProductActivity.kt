package com.example.nikestore.feature.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.common.NikeCompletable
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.Product
import com.example.nikestore.databinding.ActivityFavoriteProductBinding
import com.example.nikestore.feature.detail.ProductDetailActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteProductActivity : NikeActivity(),
    FavoriteProductAdapter.FavoriteProductEventListener {
    private lateinit var binding: ActivityFavoriteProductBinding
    private val favoriteProductAdapter: FavoriteProductAdapter by inject { parametersOf(this) }
    private val favoriteProductViewModel: FavoriteProductViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.favoriteToolBar.onBackButtonClickListener = View.OnClickListener { finish() }

        binding.favoriteProductsRv.apply {
            layoutManager =
                LinearLayoutManager(this@FavoriteProductActivity, RecyclerView.VERTICAL, false)
            adapter = favoriteProductAdapter

        }
        favoriteProductViewModel.productLiveData.observe(this) {
            if (it.isNotEmpty())
                favoriteProductAdapter.products = it as MutableList<Product>
            else
                showEmptyState(R.layout.view_default_empty_state)
            binding.root.findViewById<TextView>(R.id.emptyStateMessageTv)?.text =
                getString(R.string.favorites_empty_state_message)

        }
        favoriteProductViewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }
        binding.helpBtn.setOnClickListener {
            Snackbar.make(it, getString(R.string.favorites_help_message), Snackbar.LENGTH_LONG)
                .show()
        }
    }

    override fun onCLick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onLongClick(product: Product) {
        favoriteProductViewModel.removeFromFavorite(product)
            .asyncNetworkRequest()
            .subscribe(object : NikeCompletable(compositeDisposable) {
                override fun onComplete() {
                    favoriteProductAdapter.removeItem(product)
                }

            })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}