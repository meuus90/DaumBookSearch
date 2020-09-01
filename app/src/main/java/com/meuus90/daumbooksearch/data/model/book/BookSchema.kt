package com.meuus90.daumbooksearch.data.model.book

data class BookSchema(
    val query: String,
    val sort: String?,
    val target: String?,
    val size: Int?,
    var page: Int
)