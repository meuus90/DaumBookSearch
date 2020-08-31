package com.meuus90.daumbooksearch.data.model

import androidx.paging.PagedList
import com.meuus90.daumbooksearch.data.model.book.BookModel
import com.meuus90.daumbooksearch.data.model.book.BookResponseModel
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

object MockModel {
    val mockBook = BookModel(
        title = "미움받을 용기",
        contents = "인간은 변할 수 있고, 누구나 행복해 질 수 있다. 단 그러기 위해서는 ‘용기’가 필요하다”고 말한 철학자가 있다. 바로 프로이트, 융과 함께 ‘심리학의 3대 거장’으로 일컬어지고 있는 알프레드 아들러다. 『미움받을 용기』는 아들러 심리학에 관한 일본의 1인자 철학자 기시미 이치로와 베스트셀러 작가인 고가 후미타케의 저서로, 아들러의 심리학을 ‘대화체’로 쉽고 맛깔나게 정리하고 있다. 아들러 심리학을 공부한 철학자와 세상에 부정적이고 열등감 많은",
        url = "https://search.daum.net/search?w=bookpage&bookId=1467038&q=%EB%AF%B8%EC%9B%80%EB%B0%9B%EC%9D%84+%EC%9A%A9%EA%B8%B0",
        isbn = "8996991341 9788996991342",
        datetime = "2014-11-17T00:00:00.000+09:00",
        authors = listOf(
            "기시미 이치로",
            "고가 후미타케"
        ),
        publisher = "인플루엔셜",
        translators = listOf("전경아"),
        price = 14900,
        sale_price = 13410,
        thumbnail = "https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
        status = "정상판매"
    )

    fun mockBookResponseModel(page: Int) = BookResponseModel(
        BookResponseModel.BookMeta(
            is_end = true,
            pageable_count = 9,
            total_count = 10
        ),
        documents = getMockBookList(page)
    )

    fun getMockBookList(page: Int): MutableList<BookModel> {
        val list = mutableListOf<BookModel>()
        for (i in 0..10) {
            list.add(
                BookModel(
                    title = "미움받을 용기[${(page - 1) * 10 + i}]",
                    contents = "인간은 변할 수 있고, 누구나 행복해 질 수 있다. 단 그러기 위해서는 ‘용기’가 필요하다”고 말한 철학자가 있다. 바로 프로이트, 융과 함께 ‘심리학의 3대 거장’으로 일컬어지고 있는 알프레드 아들러다. 『미움받을 용기』는 아들러 심리학에 관한 일본의 1인자 철학자 기시미 이치로와 베스트셀러 작가인 고가 후미타케의 저서로, 아들러의 심리학을 ‘대화체’로 쉽고 맛깔나게 정리하고 있다. 아들러 심리학을 공부한 철학자와 세상에 부정적이고 열등감 많은",
                    url = "https://search.daum.net/search?w=bookpage&bookId=1467038&q=%EB%AF%B8%EC%9B%80%EB%B0%9B%EC%9D%84+%EC%9A%A9%EA%B8%B0",
                    isbn = "8996991341 9788996991342",
                    datetime = "2014-11-17T00:00:00.000+09:00",
                    authors = listOf(
                        "기시미 이치로",
                        "고가 후미타케"
                    ),
                    publisher = "인플루엔셜",
                    translators = listOf("전경아"),
                    price = 14900,
                    sale_price = 13410,
                    thumbnail = "https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                    status = "정상판매"
                )
            )
        }
        return list
    }

    val mockBookResponseEmptyModel = BookResponseModel(
        BookResponseModel.BookMeta(
            is_end = true,
            pageable_count = 9,
            total_count = 10
        ),
        documents = mutableListOf()
    )

    fun <T> mockPagedList(list: List<T>): PagedList<T> {
        val pagedList = Mockito.mock(PagedList::class.java) as PagedList<T>
        Mockito.`when`(pagedList.get(ArgumentMatchers.anyInt())).then { invocation ->
            val index = invocation.arguments.first() as Int
            list[index]
        }
        Mockito.`when`(pagedList.size).thenReturn(list.size)
        return pagedList
    }
}