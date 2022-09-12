package com.example.nikestore.data.source.comment

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.nikestore.data.Comment
import com.example.nikestore.services.http.ApiService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class CommentPagingDataSource(
    private val apiService: ApiService,
    private val compositeDisposable: CompositeDisposable,
    private val product_id: Long
) : PageKeyedDataSource<Int, Comment>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Comment>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            apiService.getComments(product_id.toString(), "1").subscribe({ response ->
                updateState(State.DONE)
                callback.onResult(response, null, 2)
            }, {
                updateState(State.ERROR)
                setRetry(Action { loadInitial(params, callback) })
            })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Comment>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Comment>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            apiService.getComments(product_id.toString(), params.key.toString()).subscribe(
                { response ->
                    updateState(State.DONE)
                    callback.onResult(response, params.key + 1)
                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadAfter(params, callback) })
                }
            )
        )
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
            )
        }
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}