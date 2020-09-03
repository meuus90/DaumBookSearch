package com.meuus90.daumbooksearch.domain.viewmodel.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.book.inDb.BooksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BooksViewModel
@Inject
constructor(
    private val repository: BooksRepository
) : ViewModel() {
    private val schemaLiveData = MutableLiveData<BookSchema>()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val books = schemaLiveData.asFlow()
        .flatMapLatest {
            repository.execute(it)
        }

    fun postBookSchema(bookSchema: BookSchema) {
        schemaLiveData.value = bookSchema
    }

    fun clearCache() = repository.clearCache()
}