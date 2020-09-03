package com.meuus90.daumbooksearch.data.repository.book

import androidx.paging.PagingData
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import kotlinx.coroutines.flow.Flow


interface BookInterface {
    fun execute(bookSchema: BookSchema): Flow<PagingData<BookDoc>>
}