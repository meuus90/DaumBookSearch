package com.meuus90.daumbooksearch.data.mock.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.meuus90.base.utility.converter.BigDecimalTypeConverter
import com.meuus90.base.utility.converter.StringListTypeConverter
import com.meuus90.daumbooksearch.BuildConfig
import com.meuus90.daumbooksearch.data.mock.model.book.BookDoc
import com.meuus90.daumbooksearch.data.mock.room.book.BookDao

/**
 * Main cache description.
 */
@Database(
    entities = [BookDoc::class], exportSchema = false, version = BuildConfig.VERSION_CODE
)
@TypeConverters(BigDecimalTypeConverter::class, StringListTypeConverter::class)
abstract class Cache : RoomDatabase() {
    abstract fun bookDao(): BookDao
}