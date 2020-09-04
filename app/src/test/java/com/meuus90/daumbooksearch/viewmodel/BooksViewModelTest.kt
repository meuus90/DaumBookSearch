package com.meuus90.daumbooksearch.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.meuus90.daumbooksearch.model.data.source.repository.book.BooksRepository
import com.meuus90.daumbooksearch.model.mock.FakeSchema
import com.meuus90.daumbooksearch.model.mock.FakeSchema.mockBookSchema0
import com.meuus90.daumbooksearch.model.mock.FakeSchema.mockBookSchema1
import com.meuus90.daumbooksearch.model.mock.FakeSchema.mockBookSchema2
import com.meuus90.daumbooksearch.model.schema.book.BookDoc
import com.meuus90.daumbooksearch.test.utils.CoroutineTestRule
import com.meuus90.daumbooksearch.test.utils.getOrAwaitValue
import com.meuus90.daumbooksearch.viewmodel.book.BooksViewModel
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
class BooksViewModelTest : TestWatcher() {
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

    @Test
    fun postBookSchemaWithDebounceTest() {
        runBlockingTest {
            viewModel.postBookSchemaWithDebounce(mockBookSchema0)
            viewModel.postBookSchemaWithDebounce(mockBookSchema1)
            viewModel.postBookSchemaWithDebounce(mockBookSchema2)

            Assert.assertEquals(
                viewModel.org.getOrAwaitValue(),
                mockBookSchema2
            )

            println("postBookSchemaWithDebounceTest() pass")
        }
    }
}