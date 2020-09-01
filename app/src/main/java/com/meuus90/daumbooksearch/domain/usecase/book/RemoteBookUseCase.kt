package com.meuus90.daumbooksearch.domain.usecase.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meuus90.base.network.Resource
import com.meuus90.base.utility.Params
import com.meuus90.base.utility.Query
import com.meuus90.daumbooksearch.data.repository.book.RemoteBookRepository
import com.meuus90.daumbooksearch.domain.usecase.BaseUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteBookUseCase
@Inject
constructor(private val repository: RemoteBookRepository) : BaseUseCase<Params, Resource>() {
    private val liveData by lazy { MutableLiveData<Query>() }

    override suspend fun execute(
        viewModelScope: CoroutineScope,
        params: Params
    ): LiveData<Resource> {
        setQuery(params)

        return repository.work(this@RemoteBookUseCase.liveData)
    }

    private fun setQuery(params: Params) {
        liveData.value = params.query
    }
}