package com.meuus90.daumbooksearch.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.meuus90.base.utility.converter.BigDecimalTypeConverter
import com.meuus90.base.utility.converter.StringListTypeConverter
import com.meuus90.daumbooksearch.BuildConfig
import com.meuus90.daumbooksearch.data.dao.book.BookDao
import com.meuus90.daumbooksearch.data.model.book.BookModel

/**
 * Main cache description.
 */
@Database(
    entities = [BookModel::class], exportSchema = false, version = BuildConfig.VERSION_CODE
)
@TypeConverters(BigDecimalTypeConverter::class, StringListTypeConverter::class)
abstract class Cache : RoomDatabase() {
    abstract fun bookDao(): BookDao
}