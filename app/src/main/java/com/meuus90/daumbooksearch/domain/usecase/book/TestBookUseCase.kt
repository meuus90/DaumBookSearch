package com.meuus90.daumbooksearch.domain.usecase.book

import com.meuus90.base.arch.Params
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.book.inDb.DbRedditPostRepository
import com.meuus90.daumbooksearch.data.room.book.BookDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestBookUseCase
@Inject
constructor(val dao: BookDao, private val repository: DbRedditPostRepository) {

//    val liveData = repository.liveData

    fun execute(params: Params) {
        val schema = params.query.datas[0] as BookSchema
        repository.postsOfSubreddit(schema)
    }

//    override fun execute(params: Params): LiveData<PagedList<BookDoc>> {
//        runBlocking {
//            dao.clear()
//        }
//
//        val config = PagedList.Config.Builder()
//            .setPageSize(AppConfig.pagedListSize)
//            .setPrefetchDistance(AppConfig.pagedListPrefetchDistance)
//            .setEnablePlaceholders(false)
//            .build()
//
//        return LivePagedListBuilder(dao.getBooksDataSourceFactory(), config)
//            .setBoundaryCallback(
//                BookBoundaryCallback(
//                    repository,
//                    params.query.datas[0] as BookSchema
//                )
//            ).build()
//    }
}