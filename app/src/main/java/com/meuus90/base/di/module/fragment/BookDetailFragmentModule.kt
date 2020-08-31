package com.meuus90.base.di.module.fragment

import com.meuus90.daumbooksearch.presentation.book.BookDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BookDetailFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeBookDetailFragment(): BookDetailFragment
}