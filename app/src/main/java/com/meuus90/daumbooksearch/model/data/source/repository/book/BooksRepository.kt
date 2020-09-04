package com.meuus90.daumbooksearch.model.data.source.repository.book

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.meuus90.base.constant.AppConfig
import com.meuus90.daumbooksearch.model.data.source.api.DaumAPI
import com.meuus90.daumbooksearch.model.data.source.local.Cache
import com.meuus90.daumbooksearch.model.paging.PagingDataInterface
import com.meuus90.daumbooksearch.model.paging.book.BooksPageKeyedMediator
import com.meuus90.daumbooksearch.model.schema.book.BookDoc
import com.meuus90.daumbooksearch.model.schema.book.BookRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BooksRepository
@Inject
constructor(private val db: Cache, private val daumAPI: DaumAPI) :
    PagingDataInterface<BookRequest, Flow<PagingData<BookDoc>>> {

    override fun execute(bookSchema: BookRequest) = Pager(
        config = PagingConfig(
            initialLoadSize = AppConfig.localInitialLoadSize,
            pageSize = AppConfig.localPagingSize,
            prefetchDistance = AppConfig.localPrefetchDistance,
            enablePlaceholders = false
        ),
        remoteMediator = BooksPageKeyedMediator(
            db,
            daumAPI,
            bookSchema
        )
    ) {
        db.bookDao().getBooksPagingSource()
    }.flow

    fun clearCache() = runBlocking { db.bookDao().clear() }
}
