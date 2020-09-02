package com.meuus90.daumbooksearch.data.paging

import androidx.paging.PagedList
import com.meuus90.base.arch.Query
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.book.BookRepository
import kotlinx.coroutines.runBlocking


class BookBoundaryCallback(
    private val repository: BookRepository,
    private val schema: BookSchema
) : PagedList.BoundaryCallback<BookDoc>() {
    private var isInitialize = true

    override fun onZeroItemsLoaded() {
        if (isInitialize) {
            schema.page = 1
            fetchBooks(schema)
            isInitialize = false
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: BookDoc) {
        if (!repository.isEnd) {
            schema.page++
            fetchBooks(schema)
        }
    }

    fun fetchBooks(schema: BookSchema) {
        runBlocking {
            repository.work(Query().init(schema))
        }
    }
}