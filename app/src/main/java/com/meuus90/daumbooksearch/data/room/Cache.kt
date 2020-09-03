package com.meuus90.daumbooksearch.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.meuus90.base.arch.converter.BigDecimalTypeConverter
import com.meuus90.base.arch.converter.StringListTypeConverter
import com.meuus90.daumbooksearch.BuildConfig
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.room.book.BookDao

/**
 * Main cache description.
 */
@Database(
    entities = [BookDoc::class],
    exportSchema = false,
    version = BuildConfig.VERSION_CODE
)
@TypeConverters(BigDecimalTypeConverter::class, StringListTypeConverter::class)
abstract class Cache : RoomDatabase() {
    abstract fun bookDao(): BookDao
}