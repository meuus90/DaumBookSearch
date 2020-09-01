package com.meuus90.daumbooksearch.domain.viewmodel.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.meuus90.base.constant.AppConfig
import com.meuus90.base.network.Resource
import com.meuus90.base.utility.Params
import com.meuus90.base.utility.Query
import com.meuus90.daumbooksearch.data.dao.book.BookDao
import com.meuus90.daumbooksearch.data.model.book.BookModel
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.domain.usecase.book.RemoteBookUseCase
import com.meuus90.daumbooksearch.domain.viewmodel.BaseViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteBookViewModel
@Inject
constructor(
    val dao: BookDao,
    private val remoteUseCase: RemoteBookUseCase
) : BaseViewModel<Params, Int>() {
    internal var book = MutableLiveData<Resource>()
    internal lateinit var bookLocal: LiveData<PagedList<BookModel>>

    private lateinit var apiParams: Params

    suspend fun callRemote(params: Params): LiveData<Resource> {
        this.apiParams = params
        return remoteUseCase.execute(viewModelScope, params)
    }

    val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(AppConfig.pagedListInitialSize)
        .setPageSize(AppConfig.pagedListSize)
        .setPrefetchDistance(AppConfig.pagedListPrefetchDistance)
        .setEnablePlaceholders(false)
        .build()

    val pagedListBuilder = LivePagedListBuilder(dao.getBooks(), config)
        .setBoundaryCallback(object : PagedList.BoundaryCallback<BookModel>() {
            override fun onZeroItemsLoaded() {
                runBlocking {
                    dao.clear()
                }
            }

            override fun onItemAtEndLoaded(itemAtEnd: BookModel) {
                runBlocking {
                    val schema = apiParams.query.params[0] as BookSchema
                    schema.page = schema.page?.plus(1)
                    apiParams = Params(Query().init(schema))

                    callRemote(apiParams)
                }
            }
        })

    override fun pullTrigger(params: Params) {
        this.apiParams = params
        runBlocking {
            dao.clear()
            callRemote(apiParams)
        }
        bookLocal = pagedListBuilder.build()
    }
}