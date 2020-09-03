package com.meuus90.daumbooksearch.data.repository.book.inDb

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.meuus90.base.arch.network.ApiSuccessResponse
import com.meuus90.daumbooksearch.data.api.DaumAPI
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.model.book.BookRemoteKey
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.room.Cache
import com.meuus90.daumbooksearch.data.room.book.BookDao
import com.meuus90.daumbooksearch.data.room.book.BookRemoteKeyDao
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val db: Cache,
    private val redditApi: DaumAPI,
    private val bookSchema: BookSchema
) : RemoteMediator<Int, BookDoc>() {
    private val postDao: BookDao = db.bookDao()
    private val remoteKeyDao: BookRemoteKeyDao = db.bookRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookDoc>
    ): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val loadKey = when (loadType) {
                REFRESH -> null
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    // Query DB for SubredditRemoteKey for the subreddit.
                    // SubredditRemoteKey is a wrapper object we use to keep track of page keys we
                    // receive from the Reddit API to fetch the next or previous page.
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.remoteKey()
                    }

                    // We must explicitly check if the page key is null when appending, since the
                    // Reddit API informs the end of the list by returning null for page key, but
                    // passing a null key to Reddit API will fetch the initial page.
                    if (remoteKey.nextPageKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextPageKey
                }
            } ?: 1

            val response = redditApi.getBookListSus(
                query = bookSchema.query,
                sort = bookSchema.sort,
                target = bookSchema.target,
                size = bookSchema.size,
                page = loadKey
            )

            var items = mutableListOf<BookDoc>()
            when (response) {
                is ApiSuccessResponse -> {

                    items = response.body.documents

                    db.withTransaction {
                        if (loadType == REFRESH) {
                            postDao.clear()
                        }

                        remoteKeyDao.insert(BookRemoteKey(loadKey + 1))
                        postDao.insert(items)
                    }
                }

//                is ApiEmptyResponse -> {
//                    result.postValue(resource.success(""))
//                }
//
//                is ApiErrorResponse -> {
//                    result.postValue(resource.error(response.errorMessage, response.statusCode))
//                    onNetworkError(response.errorMessage, response.statusCode)
//                    MediatorResult.Error(e)
//                }
            }
            return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}
