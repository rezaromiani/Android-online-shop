package com.example.nikestore.data.source.comment

import com.example.nikestore.data.Comment
import io.reactivex.Single


interface CommentDataSource {
    fun getAll(productId: Long,page:Int): Single<List<Comment>>

    fun insert(): Single<Comment>
}