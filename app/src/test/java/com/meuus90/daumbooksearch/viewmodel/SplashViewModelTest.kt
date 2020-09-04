package com.meuus90.daumbooksearch.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.meuus90.daumbooksearch.data.repository.book.inDb.BooksRepository
import com.meuus90.daumbooksearch.test.utils.CoroutineTestRule
import com.meuus90.daumbooksearch.viewmodel.splash.SplashViewModel
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestWatcher
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest : TestWatcher() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val coroutineContext = coroutineTestRule.coroutineContext

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val repository = Mockito.mock(BooksRepository::class.java) as BooksRepository

        viewModel = SplashViewModel(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun bookViewModelClearCacheTest() {
        viewModel.clearCache()

        println("bookViewModelClearCacheTest() pass")
    }
}