package com.meuus90.daumbooksearch.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.meuus90.daumbooksearch.data.mock.FakeSchema.mockBookSchema
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.repository.book.inDb.BooksRepository
import com.meuus90.daumbooksearch.test.utils.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest : TestWatcher() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val repository = Mockito.mock(BooksRepository::class.java) as BooksRepository

    private val flow = flowOf(PagingData.empty<BookDoc>())

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        Mockito.`when`(repository.execute(mockBookSchema))
            .thenReturn(flow)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun bookRepositoryTest() {
        runBlockingTest {
            Assert.assertEquals(repository.execute(mockBookSchema), flow)
//            println(repository.execute(mockBookSchema))

            println("bookRepositoryTest() pass")
        }
    }
}