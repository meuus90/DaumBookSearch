package com.meuus90.daumbooksearch.domain.viewmodel.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.meuus90.base.arch.Params
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.domain.usecase.book.BookUseCase
import com.meuus90.daumbooksearch.domain.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookViewModel
@Inject
constructor(private val useCase: BookUseCase) : BaseViewModel<Params, Int>() {
    companion object {
        const val CALL_DIRECTLY = 0
        const val CALL_DEBOUNCE = 1
        const val CALL_THROTTLE = 2
    }

    internal var book = useCase.liveData
    val livePagedList = MutableLiveData<PagedList<BookDoc>>()

    val org = MutableLiveData<Params>()

    init {
        viewModelScope.launch {
            org.asFlow()
                .debounce(300L)
                .distinctUntilChanged()
                .collect {
                    execute(it)
                }
        }
    }

    override fun pullTrigger(params: Params) {
        when (params.query.datas[1]) {
            CALL_DIRECTLY -> {
                execute(params)
            }
            CALL_DEBOUNCE -> {
//                org.postValue(params)
                org.value = params
            }
            else -> {

            }
        }
    }

    private fun execute(params: Params) {
        viewModelScope.launch {
            useCase.execute(params).asFlow()
                .collect {
                    livePagedList.value = it
                }
//            useCase.execute(params).map {
//                livePagedList.value = it
//            }
        }
    }
}