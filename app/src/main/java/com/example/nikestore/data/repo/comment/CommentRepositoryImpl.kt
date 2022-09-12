package com.example.nikestore.data.repo.comment

import com.example.nikestore.data.Comment
import com.example.nikestore.data.source.comment.CommentDataSource
import io.reactivex.Single


class CommentRepositoryImpl(private val commentDataSource: CommentDataSource) :
    CommentRepository {
    override fun getAll(productId: Long,page:Int): Single<List<Comment>> =
        commentDataSource.getAll(productId,page)

    override fun insert(): Single<Comment> {
        TODO("Not yet implemented")
    }
}