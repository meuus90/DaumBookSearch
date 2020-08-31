package com.meuus90.daumbooksearch.data.dao.book

import androidx.room.Dao
import androidx.room.Query
import com.meuus90.daumbooksearch.data.dao.BaseDao
import com.meuus90.daumbooksearch.data.model.book.BookModel

@Dao
interface BookDao : BaseDao<BookModel> {
    @Query("SELECT * FROM Book")
    fun getBooks(): MutableList<BookModel>

    @Query("DELETE FROM Book")
    suspend fun clear()
}