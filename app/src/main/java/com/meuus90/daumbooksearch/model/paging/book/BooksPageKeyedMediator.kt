package com.meuus90.daumbooksearch.model.paging.book

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.meuus90.daumbooksearch.model.data.source.api.DaumAPI
import com.meuus90.daumbooksearch.model.data.source.local.Cache
import com.meuus90.daumbooksearch.model.data.source.local.book.BookDao
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
    private var isEnd = false

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookDoc>
    ): MediatorResult {
        Timber.e("bookSchema $bookSchema")
        Timber.e("load start $loadType, $loadKey, $isEnd")
        try {
            when (loadType) {
                LoadType.REFRESH -> {
                    loadKey = 1
                    isEnd = false

                    db.withTransaction {
                        postDao.clear()
                    }
                }
                LoadType.PREPEND -> {
                }
                LoadType.APPEND -> {
                    if (!isEnd) {
                        val response = daumAPI.getBookListSus(
                            query = bookSchema.query,
                            sort = bookSchema.sort,
                            target = bookSchema.target,
                            size = bookSchema.size,
                            page = loadKey
                        )
                        Timber.e("response ${response.meta}")

                        if (response.meta.total_count == 0) {
                            val e = EmptyResultException()
                            Timber.e(e)
                            return MediatorResult.Error(e)
                        }

                        db.withTransaction {
                            postDao.insert(response.documents)
                        }

                        isEnd = response.meta.is_end
                        
                        if (!isEnd)
                            loadKey++
                    }
                }
            }
            Timber.e("load end $loadType, $loadKey, $isEnd")

            return MediatorResult.Success(endOfPaginationReached = isEnd)
        } catch (e: IOException) {
            Timber.e(e)
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            Timber.e(e)
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            Timber.e(e)
            return MediatorResult.Error(e)
        }
    }

    class EmptyResultException() : Exception()
}
