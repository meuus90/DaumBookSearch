package com.meuus90.daumbooksearch.data.repository.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meuus90.base.network.ApiResponse
import com.meuus90.base.network.NetworkBoundResource
import com.meuus90.base.network.Resource
import com.meuus90.base.utility.Query
import com.meuus90.daumbooksearch.data.dao.book.BookDao
import com.meuus90.daumbooksearch.data.model.book.BookModel
import com.meuus90.daumbooksearch.data.model.book.BookResponseModel
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.BaseRepository
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteBookRepository
@Inject
constructor(private val dao: BookDao) : BaseRepository<Query>() {
    override suspend fun work(liveData: MutableLiveData<Query>): LiveData<Resource> {
        return object :
            NetworkBoundResource<MutableList<BookModel>, BookResponseModel>() {
            override suspend fun workToCache(item: BookResponseModel) {
                //clearCache()
                dao.insert(item.documents)
            }

            override suspend fun doNetworkJob(): LiveData<ApiResponse<BookResponseModel>> {
                val schema = liveData.value?.params?.get(0) as BookSchema

                return daumAPI.getBookList(
                    query = schema.query,
                    sort = schema.sort,
                    page = schema.page,
                    size = schema.size,
                    target = schema.target
                )
            }

            override fun onNetworkError(errorMessage: String?, errorCode: Int) {
                runBlocking {
                    clearCache()
                }
                handleDefaultError(errorMessage)
                Timber.e("Network-Error: $errorMessage")
            }

            override suspend fun clearCache() = dao.clear()
        }.getAsLiveData()
    }
}