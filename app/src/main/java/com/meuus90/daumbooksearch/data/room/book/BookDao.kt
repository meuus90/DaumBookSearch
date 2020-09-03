package com.meuus90.daumbooksearch.data.room.book

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.room.BaseDao

@Dao
interface BookDao : BaseDao<BookDoc> {
    @Query("SELECT * FROM Book")
    fun getBooks(): MutableList<BookDoc>

    @Query("SELECT * FROM Book")
    fun getBooksDataSourceFactory(): DataSource.Factory<Int, BookDoc>

    @Query("SELECT * FROM Book")
    fun getBooksLiveData(): LiveData<MutableList<BookDoc>>

    @Query("SELECT * FROM Book")
    fun getBooksPagingSource(): PagingSource<Int, BookDoc>

    @Query("DELETE FROM Book")
    suspend fun clear()
}