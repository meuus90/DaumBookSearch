package com.meuus90.daumbooksearch.data.model.book

data class BookSchema(
    var query: String,
    var sort: String?,
    var target: String?,
    val size: Int?,
    var page: Int
) {

    companion object {
        const val SORT_ACCURACY = "accuracy"
        const val SORT_RECENCY = "recency"

        const val TARGET_TITLE = "title"
        const val TARGET_ISBN = "isbn"
        const val TARGET_PUBLISHER = "publisher"
        const val TARGET_PERSON = "person"
    }

    fun setQueryStr(searchStr: String) {
        query = searchStr
        page = 1
    }

    fun setSortType(index: Int) {
        sort = when (index) {
            0 -> SORT_ACCURACY
            1 -> SORT_RECENCY
            else -> null
        }
        page = 1
    }

    fun setSearchTarget(index: Int) {
        target = when (index) {
            0 -> null
            1 -> TARGET_TITLE
            2 -> TARGET_ISBN
            3 -> TARGET_PUBLISHER
            4 -> TARGET_PERSON
            else -> null
        }
        page = 1
    }
}