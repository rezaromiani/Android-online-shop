package com.example.nikestore.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeFragment
import com.example.nikestore.common.convertDpToPixel
import com.example.nikestore.data.Product
import com.example.nikestore.data.SORT_LATEST
import com.example.nikestore.data.SORT_POPULAR
import com.example.nikestore.databinding.FragmentHomeBinding
import com.example.nikestore.feature.detail.ProductDetailActivity
import com.example.nikestore.feature.home.banner.BannerSliderAdapter
import com.example.nikestore.feature.list.ProductListActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class HomeFragment : NikeFragment(), ProductListAdapter.ProductEventListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
    private val popUlarProductListAdapter: ProductListAdapter by inject {
        parametersOf(
            VIEW_TYPE_ROUND
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productListAdapter.productEventListener = this
        popUlarProductListAdapter.productEventListener = this
        binding.latestProductRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = productListAdapter
        }

        binding.popularRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = popUlarProductListAdapter
        }
        homeViewModel.productListLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            productListAdapter.products = it as ArrayList<Product>
        }

        homeViewModel.popularProductListLiveData.observe(viewLifecycleOwner) {
            popUlarProductListAdapter.products = it as ArrayList<Product>
        }

        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        homeViewModel.bannerListLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            binding.bannerSliderViewPager.adapter = bannerSliderAdapter
            binding.dotsIndicator.setViewPager2(binding.bannerSliderViewPager)
            binding.bannerSliderViewPager.post {
                val viewpagerHeight =
                    ((binding.bannerSliderViewPager.measuredWidth - convertDpToPixel(
                        32f,
                        requireContext()
                    )) * 173) / 328

                val layoutParams = binding.bannerSliderViewPager.layoutParams
                layoutParams.height = viewpagerHeight.toInt()
                binding.bannerSliderViewPager.layoutParams = layoutParams
            }
        }

        binding.viewLatestProductBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_LATEST)
            })
        }
        binding.viewPopularProductBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_POPULAR)
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onAddtoFavoriteBtnClick(product: Product) {
        homeViewModel.addToFavorite(product)
    }
}