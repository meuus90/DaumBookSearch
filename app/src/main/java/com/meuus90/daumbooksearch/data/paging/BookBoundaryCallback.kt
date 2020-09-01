package com.meuus90.daumbooksearch.data.paging

import androidx.paging.PagedList
import com.meuus90.base.utility.Query
import com.meuus90.daumbooksearch.data.model.book.BookModel
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.book.BookRepository
import kotlinx.coroutines.runBlocking


class BookBoundaryCallback(
    private val repository: BookRepository,
    private val schema: BookSchema
) : PagedList.BoundaryCallback<BookModel>() {

    override fun onZeroItemsLoaded() {
        schema.page = 1
        fetchBooks(schema)
    }

    override fun onItemAtEndLoaded(itemAtEnd: BookModel) {
        schema.page++
        fetchBooks(schema)
    }

    fun fetchBooks(schema: BookSchema) {
        runBlocking {
            repository.work(Query().init(schema))
        }
    }
}