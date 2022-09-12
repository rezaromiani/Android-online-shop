package com.example.nikestore

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.example.nikestore.data.db.AppDatabase
import com.example.nikestore.data.repo.*
import com.example.nikestore.data.repo.comment.CommentRepository
import com.example.nikestore.data.repo.comment.CommentRepositoryImpl
import com.example.nikestore.data.repo.order.OrderRepository
import com.example.nikestore.data.repo.order.OrderRepositoryImpl
import com.example.nikestore.data.repo.product.ProductRepository
import com.example.nikestore.data.repo.product.ProductRepositoryImpl
import com.example.nikestore.data.repo.user.UserRepository
import com.example.nikestore.data.repo.user.UserRepositoryImpl
import com.example.nikestore.data.source.BannerRemoteDataSource
import com.example.nikestore.data.source.CartRemoteDataSource
import com.example.nikestore.data.source.product.ProductRemoteDataSource
import com.example.nikestore.data.source.comment.CommentDataSourceRemote
import com.example.nikestore.data.source.comment.CommentPagingDataSourceFactory
import com.example.nikestore.data.source.order.OrderRemoteDataSource
import com.example.nikestore.data.source.user.UserRemoteDateSource
import com.example.nikestore.data.source.user.UserTokenDataSource
import com.example.nikestore.data.source.user.UserTokenLocalDataSource
import com.example.nikestore.feature.auth.AuthViewModel
import com.example.nikestore.feature.cart.CartViewModel
import com.example.nikestore.feature.comment.CommentListViewModel
import com.example.nikestore.feature.detail.ProductDetailViewModel
import com.example.nikestore.feature.favorite.FavoriteProductAdapter
import com.example.nikestore.feature.favorite.FavoriteProductViewModel
import com.example.nikestore.feature.home.HomeViewModel
import com.example.nikestore.feature.home.ProductListAdapter
import com.example.nikestore.feature.list.ProductListViewModel
import com.example.nikestore.feature.main.MainViewModel
import com.example.nikestore.feature.order.OrderHistoryViewModel
import com.example.nikestore.feature.paymentres.PaymentResViewModel
import com.example.nikestore.feature.profile.ProfileViewModel
import com.example.nikestore.feature.shipping.ShippingViewModel
import com.example.nikestore.services.FrescoImageLoadingService
import com.example.nikestore.services.ImageLoadingService
import com.example.nikestore.services.http.ApiService
import com.example.nikestore.services.http.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)

        val myModules = module {
            single<ApiService> { createApiServiceInstance() }
            single<ImageLoadingService> { FrescoImageLoadingService() }
            single<SharedPreferences> {
                this@App.getSharedPreferences(
                    "app_setting",
                    MODE_PRIVATE
                )
            }
            single<UserTokenDataSource> { UserTokenLocalDataSource(get()) }
            single<UserRepository> {
                UserRepositoryImpl(
                    UserRemoteDateSource(get()),
                    get()
                )
            }
            single<AppDatabase> {
                Room.databaseBuilder(
                    this@App,
                    AppDatabase::class.java,
                    "db_nike_app"
                ).build()
            }
            single<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(get())) }
            factory<ProductRepository> {
                ProductRepositoryImpl(
                    ProductRemoteDataSource(get()),
                    get<AppDatabase>().productDao()
                )
            }
            factory { parameters ->
                ProductListAdapter(
                    viewType = parameters.get(),
                    get()
                )
            }
            factory { parmetes ->
                FavoriteProductAdapter(
                    imageLoadingService = get(),
                    eventListener = parmetes.get()
                )
            }
            factory<BannerRepository> {
                BannerRepositoryImpl(BannerRemoteDataSource(get()))
            }
            factory<CommentRepository> { CommentRepositoryImpl(CommentDataSourceRemote(get())) }
            factory<CartRepository> { CartRepositoryImpl(CartRemoteDataSource(get())) }
            viewModel { HomeViewModel(get(), get()) }
            viewModel { parameters ->
                ProductDetailViewModel(
                    bundle = parameters.get(),
                    commentRepository = get(),
                    cartRepository = get()
                )
            }
            viewModel { parameters ->
                CommentListViewModel(
                    CommentPagingDataSourceFactory(
                        get(),
                        compositeDisposable = parameters.get(),
                        product_id = parameters.get()
                    )
                )
            }
            viewModel { paramerts ->
                ProductListViewModel(
                    productRepository = get(),
                    sort = paramerts.get()
                )
            }
            viewModel { AuthViewModel(get()) }
            viewModel { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { FavoriteProductViewModel(get()) }
            viewModel { OrderHistoryViewModel(get()) }
            viewModel { paremetes ->
                PaymentResViewModel(
                    orderId = paremetes.get(),
                    orderRepository = get()
                )
            }
        }
        startKoin {
            androidContext(this@App)
            modules(myModules)
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        val userRepository: UserRepository = get()
        userRepository.loadToken()
    }
}