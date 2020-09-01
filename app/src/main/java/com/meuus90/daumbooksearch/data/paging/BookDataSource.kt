package com.meuus90.daumbooksearch.data.paging

import androidx.paging.PageKeyedDataSource
import com.meuus90.daumbooksearch.data.dao.book.BookDao
import com.meuus90.daumbooksearch.data.model.book.BookModel

class BookDataSource(private val dao: BookDao) :
    PageKeyedDataSource<Int, BookModel>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, BookModel>
    ) {
        val curPage = 1
        val nextPage = curPage + 1

        callback.onResult(dao.getBooks(), null, nextPage)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, BookModel>) {
        callback.onResult(dao.getBooks(), params.key + 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, BookModel>) {
        callback.onResult(dao.getBooks(), params.key - 1)
    }
}