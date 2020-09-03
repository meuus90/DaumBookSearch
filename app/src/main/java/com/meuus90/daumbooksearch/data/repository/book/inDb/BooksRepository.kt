package com.meuus90.daumbooksearch.data.repository.book.inDb

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.meuus90.base.constant.AppConfig
import com.meuus90.daumbooksearch.data.api.DaumAPI
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.paging.PageKeyedRemoteMediator
import com.meuus90.daumbooksearch.data.repository.book.BookInterface
import com.meuus90.daumbooksearch.data.room.Cache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BooksRepository
@Inject
constructor(private val db: Cache, private val daumAPI: DaumAPI) : BookInterface {

    override fun execute(bookSchema: BookSchema) = Pager(
        config = PagingConfig(
            pageSize = bookSchema.size ?: AppConfig.pagedListSize,
            prefetchDistance = AppConfig.pagedListPrefetchDistance,
            initialLoadSize = bookSchema.size ?: AppConfig.pagedListSize,
            enablePlaceholders = false
        ),
        remoteMediator = PageKeyedRemoteMediator(
            db,
            daumAPI,
            bookSchema
        )
    ) {
        db.bookDao().getBooksPagingSource()
    }.flow
}
