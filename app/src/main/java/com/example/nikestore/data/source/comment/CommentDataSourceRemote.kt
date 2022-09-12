package com.example.nikestore.data.source.comment

import com.example.nikestore.data.Comment
import com.example.nikestore.services.http.ApiService
import io.reactivex.Single


class CommentDataSourceRemote(
    private val apiService: ApiService,
) : CommentDataSource {
    override fun getAll(productId: Long, page: Int): Single<List<Comment>> =
        apiService.getComments(productId.toString(), page.toString())

    override fun insert(): Single<Comment> {
        TODO("Not yet implemented")
    }
}