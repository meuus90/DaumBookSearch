package com.meuus90.daumbooksearch.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meuus90.base.network.Resource
import com.meuus90.daumbooksearch.data.api.DaumAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
abstract class BaseRepository<T> {
    @field:Inject
    lateinit var daumAPI: DaumAPI

    abstract suspend fun work(liveData: MutableLiveData<T>): LiveData<Resource>

    companion object {
        internal const val PER_PAGE = 20
        internal const val INDEX = 1
    }
}