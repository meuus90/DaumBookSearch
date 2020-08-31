package com.meuus90.daumbooksearch.data.api

import androidx.lifecycle.LiveData
import com.meuus90.base.network.ApiResponse
import com.meuus90.daumbooksearch.data.model.book.BookResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface DaumAPI {
    @GET("v3/search/book")
    fun getBookList(
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("target") target: String?
    ): LiveData<ApiResponse<BookResponseModel>>
}