package com.meuus90.daumbooksearch.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookModel(
    val total_count: Int
) : BaseModel(), Parcelable {
}