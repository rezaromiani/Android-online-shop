package com.example.nikestore.feature.comment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.NikeActivity
import com.example.nikestore.data.source.comment.State
import com.example.nikestore.feature.product.CommentPagingAdapter
import com.example.nikestore.view.NikeToolbar
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentListActivity : NikeActivity() {
    private val compositeDisposable = CompositeDisposable()
    private val commentListViewModel: CommentListViewModel by viewModel {
        parametersOf(
            compositeDisposable,
            intent.extras!!.getLong(EXTRA_KEY_DATA)
        )
    }
    private lateinit var commentPagingAdapter: CommentPagingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)

        commentPagingAdapter = CommentPagingAdapter()
        findViewById<RecyclerView>(R.id.commentsRv).apply {
            layoutManager =
                LinearLayoutManager(this@CommentListActivity, RecyclerView.VERTICAL, false)
            adapter = commentPagingAdapter
        }

        commentListViewModel.commentList.observe(this) {
            commentPagingAdapter.submitList(it)
        }

        commentListViewModel.getState().observe(this) { state ->
            if (commentListViewModel.listIsEmpty() && state == State.LOADING)
                setProgressIndicator(true)
            else
                setProgressIndicator(false)
        }
        findViewById<NikeToolbar>(R.id.commentListToolbar).onBackButtonClickListener =
            View.OnClickListener {
                finish()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}