package com.meuus90.daumbooksearch.model.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.meuus90.base.arch.util.converter.BigDecimalTypeConverter
import com.meuus90.base.arch.util.converter.StringListTypeConverter
import com.meuus90.base.constant.AppConfig
import com.meuus90.daumbooksearch.model.data.source.local.book.BookDao
import com.meuus90.daumbooksearch.model.schema.book.BookDoc

/**
 * Main cache description.
 */
@Database(
    entities = [BookDoc::class],
    exportSchema = false,
    version = AppConfig.roomVersionCode
)
@TypeConverters(BigDecimalTypeConverter::class, StringListTypeConverter::class)
abstract class Cache : RoomDatabase() {
    abstract fun bookDao(): BookDao
}