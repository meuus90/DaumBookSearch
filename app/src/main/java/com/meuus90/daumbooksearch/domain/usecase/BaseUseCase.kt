package com.meuus90.daumbooksearch.domain.usecase

import androidx.lifecycle.LiveData

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Result<R>].
 */
abstract class BaseUseCase<in P, T> {
    abstract fun execute(parameters: P): LiveData<T>
}