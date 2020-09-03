package com.meuus90.daumbooksearch.data.repository.book

import androidx.paging.PagingData
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import kotlinx.coroutines.flow.Flow


interface RedditPostRepository {
    fun postsOfSubreddit(bookSchema: BookSchema): Flow<PagingData<BookDoc>>

    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB
    }
}