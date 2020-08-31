package com.meuus90.daumbooksearch.data.repository.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.paging.PageKeyedDataSource
import com.meuus90.base.network.*
import com.meuus90.base.utility.Query
import com.meuus90.daumbooksearch.data.api.DaumAPI
import com.meuus90.daumbooksearch.data.model.book.BookModel
import com.meuus90.daumbooksearch.data.model.book.BookResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class BookDataSource(
    private val api: DaumAPI,
    private val query: Query,
    private val onResponse: (response: Resource) -> Unit
) :
    PageKeyedDataSource<Int, BookModel>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, BookModel>
    ) {
        val curPage = 1
        val nextPage = curPage + 1

        val apiParams = query.params

        runBlocking {
            callApi(
                api.getBookList(
                    query = apiParams[0] as String,
                    sort = apiParams[1] as String?,
                    page = curPage,
                    size = apiParams[2] as Int?,
                    target = apiParams[3] as String?
                )
            ) {
                callback.onResult(it, null, nextPage)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, BookModel>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, BookModel>
    ) {
        val apiParams = query.params

        runBlocking {
            callApi(
                api.getBookList(
                    query = apiParams[0] as String,
                    sort = apiParams[1] as String,
                    page = params.key,
                    size = apiParams[2] as Int,
                    target = apiParams[3] as String
                )
            ) {
                callback.onResult(it, params.key)
            }
        }
    }

    private suspend fun callApi(
        liveData: LiveData<ApiResponse<BookResponseModel>>,
        doOnResult: (list: MutableList<BookModel>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            liveData.asFlow()
                .collect { response ->
                    when (response) {
                        is ApiSuccessResponse -> {
                            val list = response.body.documents
                            doOnResult(list)
                        }
                        is ApiEmptyResponse -> {
                            onResponse(Resource().success(""))
                        }
                        is ApiErrorResponse -> {
                            onResponse(
                                Resource().error(
                                    response.errorMessage,
                                    response.statusCode
                                )
                            )
                        }
                    }
                }
        }
    }
}