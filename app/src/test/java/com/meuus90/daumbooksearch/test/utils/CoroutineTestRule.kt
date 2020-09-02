package com.meuus90.daumbooksearch.test.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class CoroutineTestRule : TestRule, TestCoroutineScope by TestCoroutineScope() {
    override val coroutineContext = TestCoroutineDispatcher()
    val managedCoroutineScope: ManagedCoroutineScope = TestScope(coroutineContext)

//    val dispatcher = coroutineContext[ContinuationInterceptor] as TestCoroutineDispatcher

    override fun apply(
        base: Statement, description: Description?
    ) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            Dispatchers.setMain(coroutineContext)

            base.evaluate()

            cleanupTestCoroutines()
            Dispatchers.resetMain()
        }
    }
}