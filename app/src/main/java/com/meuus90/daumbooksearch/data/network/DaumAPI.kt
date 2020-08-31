package com.meuus90.daumbooksearch.data.network

import androidx.lifecycle.LiveData
import com.meuus90.base.network.ApiResponse
import com.meuus90.daumbooksearch.data.model.BookModel
import retrofit2.http.GET

interface DaumAPI {
    @GET("v3/search/book")
    fun getPlaylists(): LiveData<ApiResponse<BookModel>>
}