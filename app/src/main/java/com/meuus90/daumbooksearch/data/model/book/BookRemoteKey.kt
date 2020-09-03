package com.meuus90.daumbooksearch.data.model.book

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.meuus90.daumbooksearch.data.model.BaseModel

@Entity(tableName = "remote_keys")
data class BookRemoteKey(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val nextPageKey: Int?
) : BaseModel()