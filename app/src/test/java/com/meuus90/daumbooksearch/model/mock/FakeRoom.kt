package com.meuus90.daumbooksearch.model.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import com.meuus90.daumbooksearch.model.data.source.local.book.BookDao
import com.meuus90.daumbooksearch.model.schema.book.BookDoc
import org.mockito.Mockito

class FakeRoom : BookDao {
    override fun getBooks(): MutableList<BookDoc> {
        return FakeModel.mockBookList
    }

    override fun getBooksLiveData(): LiveData<MutableList<BookDoc>> {
        return MutableLiveData(FakeModel.mockBookList)
    }

    override fun getBooksPagingSource(): PagingSource<Int, BookDoc> {
//        PagingData.empty<BookDoc>()
//        PagingSource.LoadParams<BookDoc>()
        return mockPagedList()
    }

    override suspend fun clear() {
    }

    override suspend fun insert(vararg obj: BookDoc) {
    }

    override suspend fun insert(obj: BookDoc) {
    }

    override suspend fun insert(obj: MutableList<BookDoc>) {
    }

    override suspend fun insert(obj: ArrayList<BookDoc>) {
    }

    override suspend fun delete(obj: BookDoc) {
    }

    override suspend fun update(vararg obj: BookDoc) {
    }

    override suspend fun update(obj: BookDoc) {
    }

    override suspend fun update(obj: MutableList<BookDoc>) {
    }

    fun mockPagedList(): PagingSource<Int, BookDoc> {
        val pagingSource = Mockito.mock(PagingSource::class.java) as PagingSource<Int, BookDoc>
//        Mockito.`when`(pagedList.get(ArgumentMatchers.anyInt())).then { invocation ->
//            val index = invocation.arguments.first() as Int
//            list[index]
//        }
//        Mockito.`when`(pagedList.size).thenReturn(list.size)
        return pagingSource
    }
}