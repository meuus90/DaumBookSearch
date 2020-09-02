package com.meuus90.daumbooksearch.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.meuus90.base.utility.Query
import com.meuus90.daumbooksearch.data.mock.MockDaumAPI
import com.meuus90.daumbooksearch.data.mock.MockModel
import com.meuus90.daumbooksearch.data.mock.MockRoom
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.book.BookRepository
import com.meuus90.daumbooksearch.test.utils.CoroutineTestRule
import com.meuus90.daumbooksearch.test.utils.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
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

    private lateinit var repository: BookRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        repository = BookRepository(MockRoom())
        repository.daumAPI = MockDaumAPI()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun bookRepositoryTest() {
        runBlockingTest {
            val resultLiveData = repository.liveData
            val query = Query().init(
                BookSchema("test", "accuracy", "title", 50, 1)
            )

            repository.work(query)

            assertEquals(resultLiveData.getOrAwaitValue().getData(), MockModel.mockResponseModel)

            println("bookRepositoryTest() pass")
        }
    }
}