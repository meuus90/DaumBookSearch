package com.meuus90.daumbooksearch.model.mock

import com.meuus90.daumbooksearch.model.data.source.api.DaumAPI
import com.meuus90.daumbooksearch.model.schema.book.BookResponse

class FakeDaumAPI : DaumAPI {
    override suspend fun getBookListSus(
        query: String,
        sort: String?,
        target: String?,
        size: Int?,
        page: Int?
    ): BookResponse {
        return FakeModel.mockResponseModel
    }
}