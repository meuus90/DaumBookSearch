package com.meuus90.daumbooksearch.data.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.paging.BookDataSource
import com.meuus90.daumbooksearch.data.room.book.BookDao

class MockRoom : BookDao {
    override fun getBooks(): MutableList<BookDoc> {
        return MockModel.mockBookList
    }

    override fun getBooksDataSourceFactory(): DataSource.Factory<Int, BookDoc> {
        return object : DataSource.Factory<Int, BookDoc>() {
            override fun create(): DataSource<Int, BookDoc> {
                return BookDataSource(this@MockRoom)
            }
        }
    }

    override fun getBooksLiveData(): LiveData<MutableList<BookDoc>> {
        return MutableLiveData(MockModel.mockBookList)
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
}