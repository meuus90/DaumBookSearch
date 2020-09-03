package com.meuus90.daumbooksearch.data.repository.book.inDb

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.meuus90.base.constant.AppConfig
import com.meuus90.daumbooksearch.data.api.DaumAPI
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.book.RedditPostRepository
import com.meuus90.daumbooksearch.data.room.Cache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbRedditPostRepository
@Inject
constructor(val db: Cache, val redditApi: DaumAPI) : RedditPostRepository {

    override fun postsOfSubreddit(bookSchema: BookSchema) = Pager(
        config = PagingConfig(bookSchema.size ?: AppConfig.pagedListSize),
        remoteMediator = PageKeyedRemoteMediator(db, redditApi, bookSchema)
    ) {
        db.bookDao().getBooksPagingSource()
    }.flow
}
