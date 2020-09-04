package com.meuus90.daumbooksearch.data.mock

import com.meuus90.daumbooksearch.data.model.book.BookSchema

object FakeSchema {
    val mockBookSchema = BookSchema(
        query = "test",
        sort = BookSchema.SORT_ACCURACY,
        target = BookSchema.TARGET_TITLE,
        size = 50,
        refreshCount = 0
    )

    val mockBookSchema0 = BookSchema("test0", null, null, null, 0)
    val mockBookSchema1 = BookSchema("test1", null, null, null, 0)
    val mockBookSchema2 = BookSchema("test2", null, null, null, 0)
}