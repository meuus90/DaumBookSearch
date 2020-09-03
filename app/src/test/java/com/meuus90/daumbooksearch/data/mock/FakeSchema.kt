package com.meuus90.daumbooksearch.data.mock

import com.meuus90.daumbooksearch.data.model.book.BookSchema

object FakeSchema {
    val mockBookSchema = BookSchema(
        query = "test",
        sort = BookSchema.SORT_ACCURACY,
        target = BookSchema.TARGET_TITLE,
        size = 50
    )
}