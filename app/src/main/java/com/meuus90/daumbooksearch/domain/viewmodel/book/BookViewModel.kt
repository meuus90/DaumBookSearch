package com.meuus90.daumbooksearch.domain.viewmodel.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.meuus90.base.utility.Params
import com.meuus90.base.utility.network.Resource
import com.meuus90.daumbooksearch.data.model.book.BookModel
import com.meuus90.daumbooksearch.domain.usecase.book.BookUseCase
import com.meuus90.daumbooksearch.domain.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookViewModel
@Inject
constructor(private val useCase: BookUseCase) : BaseViewModel<Params, Int>() {
    internal var book = MutableLiveData<Resource>()
    lateinit var livePagedList: LiveData<PagedList<BookModel>>


    override fun pullTrigger(params: Params) {
        viewModelScope.launch {
            book = useCase.liveData
            livePagedList = useCase.execute(params)
        }
    }
}