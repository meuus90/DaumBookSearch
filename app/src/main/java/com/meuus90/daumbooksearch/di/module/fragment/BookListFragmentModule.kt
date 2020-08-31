package com.meuus90.daumbooksearch.di.module.fragment

import com.meuus90.daumbooksearch.presentation.book.BookListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BookListFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeBookListFragment(): BookListFragment
}