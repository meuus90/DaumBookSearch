package com.meuus90.base.di.module.activity

import com.meuus90.base.di.module.fragment.BookDetailFragmentModule
import com.meuus90.base.di.module.fragment.BookListFragmentModule
import com.meuus90.daumbooksearch.presentation.book.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
            BookListFragmentModule::class,
            BookDetailFragmentModule::class
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}