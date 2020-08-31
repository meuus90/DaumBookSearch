package com.meuus90.daumbooksearch.domain.usecase.book

import androidx.lifecycle.MutableLiveData
import com.meuus90.base.network.Resource
import com.meuus90.base.utility.Params
import com.meuus90.base.utility.Query
import com.meuus90.daumbooksearch.data.repository.book.BookRepository
import com.meuus90.daumbooksearch.domain.usecase.BaseUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookUseCase
@Inject
constructor(private val repository: BookRepository) : BaseUseCase<Params, Resource>() {
    private val liveData by lazy { MutableLiveData<Query>() }

    override suspend fun execute(
        viewModelScope: CoroutineScope,
        params: Params
    ): MutableLiveData<Resource> {
        setQuery(params)

        return repository.work(this@BookUseCase.liveData)
    }

    private fun setQuery(params: Params) {
        liveData.value = params.query
    }
}