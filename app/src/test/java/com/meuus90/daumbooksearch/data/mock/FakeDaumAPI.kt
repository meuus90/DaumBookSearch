package com.meuus90.daumbooksearch.data.mock

import com.meuus90.daumbooksearch.data.api.DaumAPI
import com.meuus90.daumbooksearch.data.model.book.BookResponse

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