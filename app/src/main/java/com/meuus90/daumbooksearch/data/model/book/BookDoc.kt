package com.meuus90.daumbooksearch.data.model.book

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.meuus90.daumbooksearch.data.model.BaseModel
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
@Entity(tableName = "Book")
data class BookDoc(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int,
    @field:ColumnInfo(name = "isbn") val isbn: String,
    @field:ColumnInfo(name = "title") val title: String,
    @field:ColumnInfo(name = "contents") val contents: String,
    @field:ColumnInfo(name = "url") val url: String,
    @field:ColumnInfo(name = "datetime") val datetime: String,
    @field:ColumnInfo(name = "authors") val authors: List<String>,
    @field:ColumnInfo(name = "publisher") val publisher: String,
    @field:ColumnInfo(name = "translators") val translators: List<String>,
    @field:ColumnInfo(name = "price") val price: BigDecimal,
    @field:ColumnInfo(name = "sale_price") val sale_price: BigDecimal,
    @field:ColumnInfo(name = "thumbnail") val thumbnail: String,
    @field:ColumnInfo(name = "status") val status: String,
    var position: Int?
) : BaseModel(), Parcelable