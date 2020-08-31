package com.meuus90.daumbooksearch.data.repository.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.meuus90.base.constant.AppConfig
import com.meuus90.base.network.Resource
import com.meuus90.base.utility.Query
import com.meuus90.daumbooksearch.data.model.book.BookModel
import com.meuus90.daumbooksearch.data.repository.BaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository
@Inject
constructor() : BaseRepository<Query>() {
    override suspend fun work(liveData: MutableLiveData<Query>): LiveData<Resource> {
        val result = MediatorLiveData<Resource>()

        val event = MutableLiveData<Resource>()
//        resultEvent.value = Resource().loading("loading")

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(
                (liveData.value?.params?.get(2) as Int?) ?: AppConfig.pagedListInitialSize
            )
            .setPageSize(
                (liveData.value?.params?.get(2) as Int?) ?: AppConfig.pagedListInitialSize
            )
            .setPrefetchDistance(AppConfig.pagedListPrefetchDistance)
            .setEnablePlaceholders(false)
            .build()

        val livePagedList = LivePagedListBuilder(object : DataSource.Factory<Int, BookModel>() {
            override fun create(): DataSource<Int, BookModel> {
                return BookDataSource(daumAPI, liveData.value as Query) { resource ->
                    event.value = resource
//                    resultEvent.postValue(resource)
                }
            }
        }, config).build()

        result.addSource(event) { resource ->
            result.postValue(resource)
        }

        result.addSource(livePagedList) { pagedList ->
            result.postValue(Resource().success(pagedList))
        }

        return result
    }
}