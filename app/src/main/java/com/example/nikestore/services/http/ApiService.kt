package com.example.nikestore.services.http

import com.example.nikestore.data.*
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("product/list")
    fun getProduct(@Query("sort") sort: String): Single<List<Product>>

    @GET("banner/slider")
    fun getBanner(): Single<List<Banner>>

    @GET("comment/list")
    fun getComments(
        @Query("product_id") productId: String,
        @Query("page") page: String
    ): Single<List<Comment>>

    @POST("cart/add")
    fun addToCart(@Body jsonObject: JsonObject): Single<AddToCartResponse>

    @GET("cart/list")
    fun getCart(): Single<CartResponse>

    @POST("cart/remove")
    fun removeItemFromCart(@Body jsonObject: JsonObject): Single<MessageResponse>

    @POST("cart/changeCount")
    fun changeCount(@Body jsonObject: JsonObject): Single<AddToCartResponse>

    @GET("cart/count")
    fun getCartItemCount(): Single<CartItemCount>

    @POST("auth/token")
    fun getToken(@Body jsonObject: JsonObject): Single<TokenResponse>

    @POST("auth/token")
    fun refreshToken(@Body jsonObject: JsonObject): Call<TokenResponse>

    @POST("user/register")
    fun UserSignUp(@Body jsonObject: JsonObject): Single<MessageResponse>

    @POST("order/submit")
    fun submitOreder(@Body jsonObject: JsonObject): Single<SubmitOrderResult>

    @GET("order/checkout")
    fun paymentRes(@Query("order_id") orderId: Int): Single<PaymentRes>

    @GET("order/list")
    fun getOrders(): Single<List<OrderHistoryItem>>
}

fun createApiServiceInstance(): ApiService {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val oldRequest = it.request()
            val newRequestBuilder = oldRequest.newBuilder()
            if (TokenContainer.token != null)
                newRequestBuilder.addHeader("Authorization", "Bearer ${TokenContainer.token}")
            newRequestBuilder.addHeader("Accept", "application/json")
            newRequestBuilder.method(oldRequest.method, oldRequest.body)
            return@addInterceptor it.proceed(newRequestBuilder.build())

        }
        .authenticator(NikeAuthenticator())
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }).build()
    val retrofit = Retrofit.Builder()
        .baseUrl("http://expertdevelopers.ir/api/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)
}