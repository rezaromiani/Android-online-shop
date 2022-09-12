package com.example.nikestore.feature.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.nikestore.common.NikeViewModel
import com.example.nikestore.data.Comment
import com.example.nikestore.data.source.comment.CommentPagingDataSource
import com.example.nikestore.data.source.comment.CommentPagingDataSourceFactory
import com.example.nikestore.data.source.comment.State
import com.example.nikestore.services.http.createApiServiceInstance

class CommentListViewModel(private var commentPagingDataSourceFactory: CommentPagingDataSourceFactory) : NikeViewModel() {

    var commentList: LiveData<PagedList<Comment>>

    init {

        val config = PagedList.Config.Builder()
            .setPageSize(12)
            .setInitialLoadSizeHint(24)
            .setEnablePlaceholders(false)
            .build()
        commentList =
            LivePagedListBuilder<Int, Comment>(commentPagingDataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> =
        Transformations.switchMap<CommentPagingDataSource, State>(
            commentPagingDataSourceFactory.commentPagingDataSourceLiveData,
            CommentPagingDataSource::state
        )


    fun listIsEmpty(): Boolean {
        return commentList.value?.isEmpty() ?: true
    }
}