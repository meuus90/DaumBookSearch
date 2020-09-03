package com.meuus90.daumbooksearch.data.room.book

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.model.book.BookRemoteKey
import com.meuus90.daumbooksearch.data.room.BaseDao

@Dao
interface BookRemoteKeyDao : BaseDao<BookDoc> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: BookRemoteKey)

    @Query("SELECT * FROM remote_keys")
    suspend fun remoteKey(): BookRemoteKey

    @Query("DELETE FROM remote_keys")
    suspend fun deleteBySubreddit()
}