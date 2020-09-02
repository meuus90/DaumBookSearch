package com.meuus90.daumbooksearch.domain.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.meuus90.base.arch.Params
import com.meuus90.base.arch.Query
import com.meuus90.daumbooksearch.data.mock.MockDaumAPI
import com.meuus90.daumbooksearch.data.mock.MockModel
import com.meuus90.daumbooksearch.data.mock.MockRoom
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.repository.book.BookRepository
import com.meuus90.daumbooksearch.domain.usecase.book.BookUseCase
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel.Companion.CALL_DEBOUNCE
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel.Companion.CALL_DIRECTLY
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
class ViewModelTest : TestWatcher() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: BookViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val repository = BookRepository(MockRoom())
        repository.daumAPI = MockDaumAPI()
        val useCase = BookUseCase(MockRoom(), repository)

        viewModel = BookViewModel(useCase)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun bookViewModelDirectlyTest() {
        runBlockingTest {
            val livePagedList: LiveData<PagedList<BookDoc>> by lazy {
                viewModel.livePagedList
            }

            val params = Params(
                Query().init(
                    BookSchema("test", "accuracy", "title", 50, 1),
                    CALL_DIRECTLY
                )
            )

            viewModel.pullTrigger(params)

            Assert.assertEquals(
                livePagedList.getOrAwaitValue()[0],
                MockModel.mockBookList[0]
            )

            println("bookViewModelDirectlyTest() pass")
        }
    }

    @Test
    fun bookViewModelDebounceTest() {
        runBlockingTest {
            val resultLiveData = viewModel.book
            val livePagedList: LiveData<PagedList<BookDoc>> by lazy {
                viewModel.livePagedList
            }

            val params0 = Params(
                Query().init(
                    BookSchema("t", "accuracy", "title", 50, 1),
                    CALL_DEBOUNCE
                )
            )

            val params1 = Params(
                Query().init(
                    BookSchema("te", "accuracy", "title", 50, 1),
                    CALL_DEBOUNCE
                )
            )

            val params2 = Params(
                Query().init(
                    BookSchema("test", "accuracy", "title", 50, 1),
                    CALL_DEBOUNCE
                )
            )

            viewModel.org.value = params0
            viewModel.org.value = params1
            viewModel.org.value = params2

            Assert.assertEquals(
                viewModel.org.getOrAwaitValue(),
                params2
            )

            println("bookViewModelDebounceTest() pass")
        }
    }
}