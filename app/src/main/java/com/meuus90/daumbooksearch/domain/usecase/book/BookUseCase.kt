package com.meuus90.daumbooksearch.domain.usecase.book

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.meuus90.base.constant.AppConfig
import com.meuus90.base.utility.Params
import com.meuus90.base.utility.Query
import com.meuus90.daumbooksearch.data.mock.model.book.BookDoc
import com.meuus90.daumbooksearch.data.mock.model.book.BookSchema
import com.meuus90.daumbooksearch.data.mock.room.book.BookDao
import com.meuus90.daumbooksearch.data.paging.BookBoundaryCallback
import com.meuus90.daumbooksearch.data.repository.book.BookRepository
import com.meuus90.daumbooksearch.domain.usecase.BaseUseCase
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookUseCase
@Inject
constructor(val dao: BookDao, private val repository: BookRepository) :
    BaseUseCase<Params, PagedList<BookDoc>>() {

    val liveData = repository.liveData

    override fun execute(params: Params): LiveData<PagedList<BookDoc>> {
        runBlocking {
            dao.clear()
        }
        val schema = params.query.datas[0] as BookSchema

        val config = PagedList.Config.Builder()
            .setPageSize(AppConfig.pagedListSize)
            .setPrefetchDistance(AppConfig.pagedListPrefetchDistance)
            .setEnablePlaceholders(false)
            .build()

        val livePagedList = LivePagedListBuilder(dao.getBooksDataSourceFactory(), config)
            .setBoundaryCallback(
                BookBoundaryCallback(
                    repository,
                    schema
                )
            ).build()

        runBlocking {
            repository.work(Query().init(schema))
        }

        return livePagedList
    }
}