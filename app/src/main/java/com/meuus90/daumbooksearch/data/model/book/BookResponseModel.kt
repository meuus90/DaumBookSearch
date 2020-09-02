package com.meuus90.daumbooksearch.data.model.book

import com.meuus90.daumbooksearch.data.model.BaseModel

data class BookResponseModel(
    val meta: BookMeta,
    val documents: MutableList<BookDoc>
) : BaseModel() {

    data class BookMeta(
        val is_end: Boolean,
        val pageable_count: Int,
        val total_count: Int
    ) : BaseModel()

}