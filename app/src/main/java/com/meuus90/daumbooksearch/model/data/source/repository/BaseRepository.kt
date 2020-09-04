package com.meuus90.daumbooksearch.model.data.source.repository

import com.meuus90.daumbooksearch.model.data.source.api.DaumAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
abstract class BaseRepository<T> {
    @field:Inject
    lateinit var daumAPI: DaumAPI

    abstract suspend fun work(query: T)

    protected fun handleDefaultError(errorMessage: String?) {
    }
}