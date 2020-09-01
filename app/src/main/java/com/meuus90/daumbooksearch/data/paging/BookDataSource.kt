package com.meuus90.daumbooksearch.data.paging

import androidx.paging.PageKeyedDataSource
import com.meuus90.daumbooksearch.data.model.book.BookModel

class BookDataSource(private val books: MutableList<BookModel>) :
    PageKeyedDataSource<Int, BookModel>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, BookModel>
    ) {
        callback.onResult(books, null, null)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, BookModel>) {
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, BookModel>) {
    }
}