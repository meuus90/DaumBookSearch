package com.meuus90.daumbooksearch.data.model.book

data class BookSchema(
    val query: String,
    val sort: String?,
    var page: Int?,
    val size: Int?,
    val target: String?
)