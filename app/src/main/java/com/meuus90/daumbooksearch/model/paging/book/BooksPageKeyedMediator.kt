package com.meuus90.daumbooksearch.model.paging.book

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.meuus90.daumbooksearch.model.data.source.local.Cache
import com.meuus90.daumbooksearch.model.data.source.local.book.BookDao
import com.meuus90.daumbooksearch.model.data.source.remote.api.DaumAPI
import com.meuus90.daumbooksearch.model.schema.book.BookDoc
import com.meuus90.daumbooksearch.model.schema.book.BookRequest
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BooksPageKeyedMediator(
    private val db: Cache,
    private val daumAPI: DaumAPI,
    private val bookSchema: BookRequest
) : RemoteMediator<Int, BookDoc>() {
    private val postDao: BookDao = db.bookDao()

    private var loadKey = 1
    private var is_end = false

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookDoc>
    ): MediatorResult {
        Timber.e("PageKeyedRemoteMediator.load $loadType, $loadKey")
        try {
            when (loadType) {
                LoadType.REFRESH -> {
                    loadKey = 1
                    is_end = false
                    Timber.e("PageKeyedRemoteMediator.schema $bookSchema")

                    db.withTransaction {
                        postDao.clear()
                    }

                    return MediatorResult.Success(endOfPaginationReached = is_end)
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = is_end)
                }
                LoadType.APPEND -> {
                    if (!is_end) {
                        var items = mutableListOf<BookDoc>()

                        val response = daumAPI.getBookListSus(
                            query = bookSchema.query,
                            sort = bookSchema.sort,
                            target = bookSchema.target,
                            size = bookSchema.size,
                            page = loadKey
                        )
                        Timber.e("PageKeyedRemoteMediator.response ${response.meta}")

                        if (response.meta.total_count == 0) {
                            return MediatorResult.Error(EmptyResultException())
                        }

                        items = response.documents

                        db.withTransaction {
                            postDao.insert(items)
                        }

                        is_end = response.meta.is_end

//                    if (!is_end) {
                        loadKey++
                    }

//                    return MediatorResult.Success(endOfPaginationReached = items.isEmpty() || response.meta.is_end)
                    return MediatorResult.Success(endOfPaginationReached = is_end)
                }
            }
        } catch (e: IOException) {
            is_end = true
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            is_end = true
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            is_end = true
            return MediatorResult.Error(e)
        }
    }

    class EmptyResultException() : Exception()
}
