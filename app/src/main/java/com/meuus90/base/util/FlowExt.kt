package com.meuus90.base.util

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow


fun <T> Flow<T>.customDebounce(waitMillis: Long) = flow {
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