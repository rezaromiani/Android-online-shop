package com.example.nikestore.data.source.comment

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.nikestore.data.Comment
import com.example.nikestore.services.http.ApiService
import io.reactivex.disposables.CompositeDisposable

class CommentPagingDataSourceFactory(
    private val apiService: ApiService,
    private val compositeDisposable: CompositeDisposable,
    private val product_id: Long
) : DataSource.Factory<Int, Comment>() {

    val commentPagingDataSourceLiveData = MutableLiveData<CommentPagingDataSource>()

    override fun create(): DataSource<Int, Comment> {
        val commentPagingDataSource =
            CommentPagingDataSource(apiService, compositeDisposable, product_id)
        commentPagingDataSourceLiveData.postValue(commentPagingDataSource)
        return commentPagingDataSource
    }

}