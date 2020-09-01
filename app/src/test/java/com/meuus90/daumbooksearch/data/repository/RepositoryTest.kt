package com.meuus90.daumbooksearch.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.meuus90.base.utility.Query
import com.meuus90.base.utility.network.Resource
import com.meuus90.daumbooksearch.data.model.book.BookModel
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class RepositoryTest : TestWatcher() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var repository: RemoteBookRepository

    val liveData =
        MutableLiveData<Query>(Query().setParams("test", "accuracy", 10, "title"))

    var page = 1

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)

        repository = mock(RemoteBookRepository::class.java)

        val result = MutableLiveData(
            Resource().success(
                mock(PagedList::class.java) as PagedList<BookModel>
            )
        )

        runBlockingTest {
            `when`(repository.work(liveData)).thenReturn(result)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun mockRepoTest() {
        println("test")

        var result: MutableLiveData<Resource>? = null
        runBlocking {
            result = repository.work(liveData)
        }
        println(result)


//            Assert.assertTrue(result.value?.getData() is PagedList<*>)

    }
}