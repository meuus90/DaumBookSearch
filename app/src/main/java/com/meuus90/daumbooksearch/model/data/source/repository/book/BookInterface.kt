package com.meuus90.daumbooksearch.model.data.source.repository.book

import androidx.paging.PagingData
import com.meuus90.daumbooksearch.model.schema.book.BookDoc
import com.meuus90.daumbooksearch.model.schema.book.BookRequest
import kotlinx.coroutines.flow.Flow


interface BookInterface {
    fun execute(bookSchema: BookRequest): Flow<PagingData<BookDoc>>
}