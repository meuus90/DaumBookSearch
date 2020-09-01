package com.meuus90.base.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.meuus90.base.utility.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor() {
    private val result = MediatorLiveData<Resource>()
    private val resource = Resource()

    private var cacheData: LiveData<ResultType>? = null

    init {
        resource.loading(null)
        runBlocking {
            handleDataFromNetwork()
        }
    }

    suspend fun handleDataFromNetwork() {
        val responseData = doNetworkJob()

        result.postValue(resource.loading("loading"))
        result.addSource(responseData) { response ->
            result.removeSource(responseData)
            
            when (response) {
                is ApiSuccessResponse -> {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            workToCache(response.body)
                            cacheData = loadFromCache(false)
                        }

                        if (cacheData != null) {
                            result.addSource(cacheData!!) { newData ->
                                result.postValue(resource.success(newData))
                            }
                        } else {
                            result.postValue(resource.success(response.body))
                        }
                    }
                }

                is ApiEmptyResponse -> {
                    result.postValue(resource.success(""))
                }

                is ApiErrorResponse -> {
                    failed(response)
                }
            }
        }
    }

    private fun failed(response: ApiErrorResponse<RequestType>) {
        result.postValue(resource.error(response.errorMessage, response.statusCode))
//        result.value = resource.error(response.errorMessage, response.statusCode)
        onNetworkError(response.errorMessage, response.statusCode)
    }

    @WorkerThread
    protected open suspend fun workToCache(item: RequestType) {
    }

    @WorkerThread
    protected open suspend fun loadFromCache(isLatest: Boolean): LiveData<ResultType>? {
        return cacheData
    }

    @WorkerThread
    protected abstract suspend fun doNetworkJob(): LiveData<ApiResponse<RequestType>>

    protected abstract fun onNetworkError(errorMessage: String?, errorCode: Int)

    @WorkerThread
    protected open suspend fun clearCache() {
    }

    fun getAsLiveData(): MediatorLiveData<Resource> {
        return result
    }

    fun getAsSingleLiveEvent(): SingleLiveEvent<Resource> {
        val resultEvent = SingleLiveEvent<Resource>()
        resultEvent.addSource(result) {
            resultEvent.value = it
        }
        return resultEvent
    }
}