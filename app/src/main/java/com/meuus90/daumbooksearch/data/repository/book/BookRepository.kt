package com.meuus90.daumbooksearch.data.repository.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.meuus90.base.arch.Query
import com.meuus90.base.arch.network.ApiResponse
import com.meuus90.base.arch.network.NetworkBoundResource
import com.meuus90.base.arch.network.Resource
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.model.book.BookResponseModel
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.BaseRepository
import com.meuus90.daumbooksearch.data.room.book.BookDao
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository
@Inject
constructor(val dao: BookDao) : BaseRepository<Query>() {
    var liveData = MediatorLiveData<Resource>()

    fun clearCache() = runBlocking { dao.clear() }

    var isEnd = false

    override suspend fun work(query: Query) {
        val schema = query.datas[0] as BookSchema

        liveData.addSource(
            object : NetworkBoundResource<MutableList<BookDoc>, BookResponseModel>() {
                override suspend fun workToCache(item: BookResponseModel) {
                    isEnd = item.meta.is_end
                    dao.insert(item.documents)
                }

                override suspend fun doNetworkJob(): LiveData<ApiResponse<BookResponseModel>> {
                    return daumAPI.getBookList(
                        query = schema.query,
                        sort = schema.sort,
                        target = schema.target,
                        size = schema.size,
                        page = schema.page
                    )
                }

                override fun onNetworkError(errorMessage: String?, errorCode: Int) {
                }
            }.getAsLiveData()
        ) {
            liveData.postValue(it)
        }
    }
}