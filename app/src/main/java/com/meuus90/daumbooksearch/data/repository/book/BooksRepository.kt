package com.meuus90.daumbooksearch.data.repository.book

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.meuus90.base.constant.AppConfig
import com.meuus90.daumbooksearch.data.api.DaumAPI
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.paging.PageKeyedRemoteMediator
import com.meuus90.daumbooksearch.data.room.Cache
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BooksRepository
@Inject
constructor(private val db: Cache, private val daumAPI: DaumAPI) : BookInterface {

    override fun execute(bookSchema: BookSchema) = Pager(
        config = PagingConfig(
            initialLoadSize = AppConfig.localInitialLoadSize,
            pageSize = AppConfig.localPagingSize,
            prefetchDistance = AppConfig.localPrefetchDistance,
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

    fun clearCache() = runBlocking { db.bookDao().clear() }
}
