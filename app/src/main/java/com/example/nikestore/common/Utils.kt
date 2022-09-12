package com.example.nikestore.common

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import androidx.browser.customtabs.CustomTabsIntent
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.NumberFormat

fun convertDpToPixel(dp: Float, context: Context?): Float {
    return if (context != null) {
        val resources = context.resources
        val metrics = resources.displayMetrics
        dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    } else {
        val metrics = Resources.getSystem().displayMetrics
        dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}

fun formatPrice(
    price: Number,
    unitRelativeSizeFactor: Float = 0.7f
): SpannableString {

    val numberFormat = NumberFormat.getInstance()
    numberFormat.maximumFractionDigits = 0

    val currencyLabel = "تومان"
    val spannableString = SpannableString("${numberFormat.format(price)} $currencyLabel")
    spannableString.setSpan(
        RelativeSizeSpan(unitRelativeSizeFactor),
        spannableString.indexOf(currencyLabel),
        spannableString.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

fun Completable.asyncNetworkRequest(): Completable {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.asyncNetworkRequest(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun openUrlInCustomTab(context: Context, url: String) {
    try {
        val uri = Uri.parse(url)
        val intentBuilder = CustomTabsIntent.Builder()
        intentBuilder.setStartAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
        intentBuilder.setExitAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)

        val customTabsIntent = intentBuilder.build()
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        customTabsIntent.launchUrl(context, uri)
    } catch (e: Exception) {
        Timber.e(e)
    }
}