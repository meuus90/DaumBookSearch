package com.meuus90.daumbooksearch.di.module.fragment

import com.meuus90.daumbooksearch.ui.book.BookDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BookDetailFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeBookDetailFragment(): BookDetailFragment
}