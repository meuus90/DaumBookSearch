package com.meuus90.daumbooksearch.domain.viewmodel.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.meuus90.base.network.Resource
import com.meuus90.base.utility.Params
import com.meuus90.daumbooksearch.domain.usecase.book.BookUseCase
import com.meuus90.daumbooksearch.domain.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookViewModel
@Inject
constructor(private val useCase: BookUseCase) : BaseViewModel<Params, Int>() {
    internal lateinit var book: LiveData<Resource>

    override fun pullTrigger(params: Params) {
        viewModelScope.launch {
            book = useCase.execute(viewModelScope, params)
        }
    }
}