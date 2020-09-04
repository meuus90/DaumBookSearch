package com.meuus90.daumbooksearch.model.mock

import com.meuus90.daumbooksearch.model.schema.book.BookRequest

object FakeSchema {
    val mockBookSchema = BookRequest(
        query = "test",
        sort = BookRequest.SORT_ACCURACY,
        target = BookRequest.TARGET_TITLE,
        size = 50,
        refreshCount = 0
    )

    val mockBookSchema0 = BookRequest("test0", null, null, null, 0)
    val mockBookSchema1 = BookRequest("test1", null, null, null, 0)
    val mockBookSchema2 = BookRequest("test2", null, null, null, 0)
}