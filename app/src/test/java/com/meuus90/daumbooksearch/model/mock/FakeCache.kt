package com.meuus90.daumbooksearch.model.mock

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.meuus90.daumbooksearch.model.data.source.local.Cache
import com.meuus90.daumbooksearch.model.data.source.local.book.BookDao
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