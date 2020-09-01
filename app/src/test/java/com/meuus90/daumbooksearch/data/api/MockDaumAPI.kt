package com.meuus90.daumbooksearch.data.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.meuus90.base.utility.network.ApiResponse
import com.meuus90.daumbooksearch.data.model.MockModel
import com.meuus90.daumbooksearch.data.model.book.BookResponseModel
import retrofit2.Response

class MockDaumAPI : DaumAPI {
    override fun getBookList(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ): LiveData<ApiResponse<BookResponseModel>> {
//        val liveData = LiveData<ApiResponse<BookResponseModel>>()
        return liveData {
            this.emit(
                ApiResponse.create(
                    Response.success(
                        MockModel.mockBookResponseModel(
                            page ?: 0
                        )
                    )
                )
            )
        }
    }
}