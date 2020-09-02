package com.meuus90.daumbooksearch.data.mock.api

import androidx.lifecycle.LiveData
import com.meuus90.base.utility.network.ApiResponse
import com.meuus90.daumbooksearch.data.mock.model.book.BookResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface DaumAPI {
    @GET("v3/search/book")
    fun getBookList(
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("target") target: String?,
        @Query("size") size: Int?,
        @Query("page") page: Int?
    ): LiveData<ApiResponse<BookResponseModel>>
}