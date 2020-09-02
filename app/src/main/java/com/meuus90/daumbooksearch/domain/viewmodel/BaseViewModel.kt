package com.meuus90.daumbooksearch.domain.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

abstract class BaseViewModel<in P, T> : ViewModel() {
    abstract fun pullTrigger(params: P)

    fun <T> Flow<T>.debounce(waitMillis: Long) = flow {
        coroutineScope {
            val context = coroutineContext
            var delayPost: Deferred<Unit>? = null
            collect {
                delayPost?.cancel()
                delayPost = async(Dispatchers.Default) {
                    delay(waitMillis)
                    withContext(context) {
                        // emit doesn't allow context changing.
                        // We should call emit on original context.
                        emit(it)
                    }
                }
            }
        }
    }
}