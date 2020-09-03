package com.meuus90.daumbooksearch.data.mock

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.meuus90.daumbooksearch.data.room.Cache
import com.meuus90.daumbooksearch.data.room.book.BookDao
import org.mockito.Mockito

class FakeCache : Cache() {
    override fun bookDao(): BookDao {
        return FakeRoom()
    }

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        return Mockito.mock(SupportSQLiteOpenHelper::class.java) as SupportSQLiteOpenHelper
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return Mockito.mock(InvalidationTracker::class.java) as InvalidationTracker
    }

    override fun clearAllTables() {
    }
}