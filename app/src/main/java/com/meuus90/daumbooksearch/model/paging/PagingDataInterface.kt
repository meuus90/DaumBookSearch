package com.meuus90.daumbooksearch.model.paging


interface PagingDataInterface<Request, Response> {
    fun execute(bookSchema: Request): Response
}