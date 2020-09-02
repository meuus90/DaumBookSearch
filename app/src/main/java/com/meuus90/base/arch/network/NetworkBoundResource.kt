package com.meuus90.base.arch.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.meuus90.base.arch.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor() {
    private val result = MediatorLiveData<Resource>()
    private val resource = Resource()

    private var cacheData: LiveData<ResultType>? = null

    init {
        runBlocking {
            handleDataFromNetwork()
        }
    }

    private suspend fun handleDataFromNetwork() {
        val responseData = doNetworkJob()

        result.postValue(resource.loading(""))
        result.addSource(responseData) { response ->
            result.removeSource(responseData)

            when (response) {
                is ApiSuccessResponse -> {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            workToCache(response.body)
                        }
                        result.postValue(resource.success(response.body))
                    }
                }

                is ApiEmptyResponse -> {
                    result.postValue(resource.success(""))
                }

                is ApiErrorResponse -> {
                    result.postValue(resource.error(response.errorMessage, response.statusCode))
                    onNetworkError(response.errorMessage, response.statusCode)
                }
            }
        }
    }

    @WorkerThread
    protected open suspend fun workToCache(item: RequestType) {
    }

    @WorkerThread
    protected abstract suspend fun doNetworkJob(): LiveData<ApiResponse<RequestType>>

    protected abstract fun onNetworkError(errorMessage: String?, errorCode: Int)

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