package com.example.nikestore.feature.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.common.NikeCompletable
import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.Comment
import com.example.nikestore.data.Product
import com.example.nikestore.databinding.ActivityProductDetailBinding
import com.example.nikestore.feature.comment.CommentListActivity
import com.example.nikestore.feature.product.CommentAdapter
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.view.scroll.ObservableScrollViewCallbacks
import com.example.nikestore.view.scroll.ScrollState
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductDetailActivity : NikeActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val productDetailViewModel: ProductDetailViewModel by viewModel { parametersOf(intent.extras) }
    private val imageLoadingService: ImageLoadingService by inject()
    private val commentAdapter: CommentAdapter = CommentAdapter()
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.i(intent.extras?.getParcelable<Product>(EXTRA_KEY_DATA).toString())
        productDetailViewModel.productLiveData.observe(this) { product ->
            binding.apply {
                imageLoadingService.load(productIv, product.image)
                productTitleTv.text = product.title
                previousPriceTv.text = formatPrice(product.previous_price)
                currentPriceTv.text = formatPrice(product.price)
                toolbarTitleTv.text = product.title
            }

        }
        binding.commentRv.apply {
            layoutManager =
                LinearLayoutManager(this@ProductDetailActivity, RecyclerView.VERTICAL, false)
            adapter = commentAdapter
        }
        productDetailViewModel.commentLiveData.observe(this) {
            commentAdapter.comments = it as ArrayList<Comment>
            if (it.size > 3) {
                binding.viewAllCommentBtn.visibility = View.VISIBLE
                binding.viewAllCommentBtn.setOnClickListener {
                    startActivity(Intent(this, CommentListActivity::class.java).apply {
                        putExtra(EXTRA_KEY_DATA, productDetailViewModel.productLiveData.value?.id)
                    })
                }
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
        productDetailViewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }
        binding.productIv.post {
            val productIvHeight = binding.productIv.height
            val productImageView = binding.productIv
            val toolbar = binding.toolbarView
            binding.observableScrollView.addScrollViewCallbacks(object :
                ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    toolbar.alpha = scrollY.toFloat() / productIvHeight.toFloat()
                    productImageView.translationY = scrollY.toFloat() / 2
                }

                override fun onDownMotionEvent() {

                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {

                }
            })
        }

        binding.addToCartBtn.setOnClickListener {
            productDetailViewModel.onAddToCartBtn().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeCompletable(compositeDisposable) {
                    override fun onComplete() {
                        Snackbar.make(
                            rootView as CoordinatorLayout,
                            "به سبد خرید اضافه شد",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}