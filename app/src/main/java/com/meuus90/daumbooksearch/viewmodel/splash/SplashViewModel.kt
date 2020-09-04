package com.meuus90.daumbooksearch.viewmodel.splash

import androidx.lifecycle.ViewModel
import com.meuus90.daumbooksearch.model.data.source.remote.book.BooksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashViewModel
@Inject
constructor(private val repository: BooksRepository) : ViewModel() {
    fun clearCache() = repository.clearCache()
}