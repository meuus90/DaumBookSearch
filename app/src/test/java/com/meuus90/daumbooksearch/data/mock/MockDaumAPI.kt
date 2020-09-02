package com.meuus90.daumbooksearch.data.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.meuus90.base.utility.network.ApiResponse
import com.meuus90.daumbooksearch.data.api.DaumAPI
import com.meuus90.daumbooksearch.data.model.book.BookResponseModel
import retrofit2.Response

class MockDaumAPI : DaumAPI {
    companion object {
        val mockResponse = Response.success(
            MockModel.mockResponseModel
        )
    }

    override fun getBookList(
        query: String,
        sort: String?,
        target: String?,
        size: Int?,
        page: Int?
    ): LiveData<ApiResponse<BookResponseModel>> {
//        val liveData = LiveData<ApiResponse<BookResponseModel>>()
        return liveData {
            this.emit(ApiResponse.create(mockResponse))
        }
    }
}