package com.meuus90.daumbooksearch.data.mock.room.book

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.meuus90.daumbooksearch.data.mock.model.book.BookDoc
import com.meuus90.daumbooksearch.data.mock.room.BaseDao

@Dao
interface BookDao : BaseDao<BookDoc> {
    @Query("SELECT * FROM Book")
    fun getBooks(): MutableList<BookDoc>

    @Query("SELECT * FROM Book")
    fun getBooksDataSourceFactory(): DataSource.Factory<Int, BookDoc>

    @Query("SELECT * FROM Book")
    fun getBooksLiveData(): LiveData<MutableList<BookDoc>>

    @Query("DELETE FROM Book")
    suspend fun clear()
}