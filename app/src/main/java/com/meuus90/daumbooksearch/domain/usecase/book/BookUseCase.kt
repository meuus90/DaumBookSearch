package com.meuus90.daumbooksearch.domain.usecase.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.meuus90.base.constant.AppConfig
import com.meuus90.base.utility.Params
import com.meuus90.base.utility.network.Resource
import com.meuus90.daumbooksearch.data.dao.book.BookDao
import com.meuus90.daumbooksearch.data.model.book.BookModel
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.paging.BookBoundaryCallback
import com.meuus90.daumbooksearch.data.repository.book.BookRepository
import com.meuus90.daumbooksearch.domain.usecase.BaseUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookUseCase
@Inject
constructor(val dao: BookDao, private val repository: BookRepository) :
    BaseUseCase<Params, PagedList<BookModel>>() {

    var liveData = MutableLiveData<Resource>()

    override fun execute(params: Params): LiveData<PagedList<BookModel>> {
        val config = PagedList.Config.Builder()
            .setPageSize(AppConfig.pagedListSize)
            .setPrefetchDistance(AppConfig.pagedListPrefetchDistance)
            .setEnablePlaceholders(true)
            .build()

        liveData = repository.liveData

        return LivePagedListBuilder(dao.getBooks(), config)
            .setBoundaryCallback(
                BookBoundaryCallback(
                    repository,
                    params.query.datas[0] as BookSchema
                )
            ).build()
    }
}