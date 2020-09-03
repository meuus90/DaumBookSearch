package com.meuus90.daumbooksearch.domain.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.meuus90.daumbooksearch.data.mock.FakeSchema
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.repository.book.inDb.BooksRepository
import com.meuus90.daumbooksearch.domain.viewmodel.book.BooksViewModel
import com.meuus90.daumbooksearch.test.utils.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
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

    private val coroutineContext = coroutineTestRule.coroutineContext

    private lateinit var viewModel: BooksViewModel

    @Mock
    private lateinit var mockObserver: Observer<PagingData<BookDoc>>

    @Captor
    private lateinit var captor: ArgumentCaptor<PagingData<BookDoc>>

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val repository = Mockito.mock(BooksRepository::class.java) as BooksRepository

        runBlockingTest {
//            val flow = flowOf(PagingData.empty<BookDoc>())
            val flow = flow<PagingData<BookDoc>> {
                emit(PagingData.empty())
            }

            Mockito.`when`(repository.execute(FakeSchema.mockBookSchema))
                .thenReturn(flow)
        }

        viewModel = BooksViewModel(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun bookViewModelDirectlyTest() {
        viewModel.postBookSchema(FakeSchema.mockBookSchema)
        val liveData = viewModel.books.asLiveData(coroutineContext)
        liveData.observeForever(mockObserver)

        verify(mockObserver).onChanged(captor.capture())
        Assert.assertTrue(captor.value.equals(PagingData.empty<BookDoc>()))

        println("bookViewModelDirectlyTest() pass")
    }

//    @Test
//    fun bookViewModelDirectlyTest() {
//        runBlockingTest {
//            val livePagedList: LiveData<PagedList<BookDoc>> by lazy {
//                viewModel.livePagedList
//            }
//
//            val params = Params(
//                Query().init(
//                    BookSchema("test", "accuracy", "title", 50, 1),
//                    CALL_DIRECTLY
//                )
//            )
//
//            viewModel.pullTrigger(params)
//
//            Assert.assertEquals(
//                livePagedList.getOrAwaitValue()[0],
//                MockModel.mockBookList[0]
//            )
//
//            println("bookViewModelDirectlyTest() pass")
//        }
//    }
//
//    @Test
//    fun bookViewModelDebounceTest() {
//        runBlockingTest {
//            val resultLiveData = viewModel.book
//            val livePagedList: LiveData<PagedList<BookDoc>> by lazy {
//                viewModel.livePagedList
//            }
//
//            val params0 = Params(
//                Query().init(
//                    BookSchema("t", "accuracy", "title", 50, 1),
//                    CALL_DEBOUNCE
//                )
//            )
//
//            val params1 = Params(
//                Query().init(
//                    BookSchema("te", "accuracy", "title", 50, 1),
//                    CALL_DEBOUNCE
//                )
//            )
//
//            val params2 = Params(
//                Query().init(
//                    BookSchema("test", "accuracy", "title", 50, 1),
//                    CALL_DEBOUNCE
//                )
//            )
//
//            viewModel.org.value = params0
//            viewModel.org.value = params1
//            viewModel.org.value = params2
//
//            Assert.assertEquals(
//                viewModel.org.getOrAwaitValue(),
//                params2
//            )
//
//            println("bookViewModelDebounceTest() pass")
//        }
//    }
}