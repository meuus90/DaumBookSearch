package com.meuus90.daumbooksearch.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.meuus90.base.utility.Params
import com.meuus90.base.utility.Query
import com.meuus90.daumbooksearch.data.mock.api.MockDaumAPI
import com.meuus90.daumbooksearch.data.mock.model.MockModel
import com.meuus90.daumbooksearch.data.mock.model.book.BookSchema
import com.meuus90.daumbooksearch.data.mock.room.MockRoom
import com.meuus90.daumbooksearch.data.repository.book.BookRepository
import com.meuus90.daumbooksearch.domain.usecase.book.BookUseCase
import com.meuus90.daumbooksearch.test.utils.CoroutineTestRule
import com.meuus90.daumbooksearch.test.utils.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import junit.framework.Assert
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
class UseCaseTest : TestWatcher() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var useCase: BookUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val repository = BookRepository(MockRoom())
        repository.daumAPI = MockDaumAPI()

        useCase = BookUseCase(MockRoom(), repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun bookUseCaseTest() {
        runBlockingTest {
            val resultLiveData = useCase.liveData
            val params = Params(
                Query().init(
                    BookSchema("test", "accuracy", "title", 50, 1)
                )
            )

            val livePagedList = useCase.execute(params)

            Assert.assertEquals(
                resultLiveData.getOrAwaitValue().getData(),
                MockModel.mockResponseModel
            )

            Assert.assertEquals(
                livePagedList.getOrAwaitValue()[0],
                MockModel.mockBookList[0]
            )

            println("bookUseCaseTest() pass")
        }
    }
}