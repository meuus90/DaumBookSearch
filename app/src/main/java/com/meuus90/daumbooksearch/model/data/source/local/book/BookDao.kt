package com.meuus90.daumbooksearch.model.data.source.local.book

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.meuus90.daumbooksearch.model.data.source.local.BaseDao
import com.meuus90.daumbooksearch.model.schema.book.BookDoc

@Dao
interface BookDao : BaseDao<BookDoc> {
    @Query("SELECT * FROM Book")
    fun getBooks(): MutableList<BookDoc>

    @Query("SELECT * FROM Book")
    fun getBooksLiveData(): LiveData<MutableList<BookDoc>>

    @Query("SELECT * FROM Book")
    fun getBooksPagingSource(): PagingSource<Int, BookDoc>

    @Query("DELETE FROM Book")
    suspend fun clear()
}