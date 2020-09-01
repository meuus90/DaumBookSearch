package com.meuus90.daumbooksearch.data.repository.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.meuus90.base.utility.Query
import com.meuus90.base.utility.network.ApiResponse
import com.meuus90.base.utility.network.NetworkBoundResource
import com.meuus90.base.utility.network.Resource
import com.meuus90.daumbooksearch.data.dao.book.BookDao
import com.meuus90.daumbooksearch.data.model.book.BookModel
import com.meuus90.daumbooksearch.data.model.book.BookResponseModel
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.BaseRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository
@Inject
constructor(val dao: BookDao) : BaseRepository<Query>() {
    var liveData = MediatorLiveData<Resource>()

    fun clearCache() = runBlocking { dao.clear() }

    override suspend fun work(query: Query) {
        val schema = query.datas[0] as BookSchema

        liveData.addSource(
            object : NetworkBoundResource<MutableList<BookModel>, BookResponseModel>() {
                override suspend fun workToCache(item: BookResponseModel) {
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
//            if (it.getStatus() == Status.SUCCESS) {
//                val item = it.getData() as BookResponseModel
//                runBlocking {
//                    dao.insert(item.documents)
//                }
//            }
            liveData.postValue(it)
        }
    }
}