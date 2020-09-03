package com.meuus90.daumbooksearch.di.module.activity

import com.meuus90.daumbooksearch.di.module.fragment.BookDetailFragmentModule
import com.meuus90.daumbooksearch.di.module.fragment.BookListFragmentModule
import com.meuus90.daumbooksearch.ui.MainActivity
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