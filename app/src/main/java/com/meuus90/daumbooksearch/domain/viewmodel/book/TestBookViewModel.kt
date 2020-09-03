package com.meuus90.daumbooksearch.domain.viewmodel.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.paging.PagingData
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.book.inDb.DbRedditPostRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestBookViewModel
@Inject
constructor(
    private val repository: DbRedditPostRepository
) : ViewModel() {
    val schema = MutableLiveData<BookSchema>()

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val posts = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty<BookDoc>() },
        schema.asFlow()
            .flatMapLatest {
                repository.postsOfSubreddit(it)
            }
    ).flattenMerge(2)

    fun shouldShowSubreddit(
        bookSchema: BookSchema
    ) = schema.value != bookSchema

    fun showSubreddit(bookSchema: BookSchema) {
        if (!shouldShowSubreddit(bookSchema)) return

        clearListCh.offer(Unit)

        schema.value = bookSchema
    }
}