package com.meuus90.daumbooksearch.data.paging

import androidx.paging.PagedList

class PagedListBoundaryCallback<T>(private val onItemAtEnd: (requestPage: Int) -> Unit) :
    PagedList.BoundaryCallback<T>() {

    companion object {
        private var requestPage = 1
    }

    override fun onZeroItemsLoaded() {
        requestPage = 1
    }

    override fun onItemAtEndLoaded(itemAtEnd: T) {
        onItemAtEnd(++requestPage)
    }
}