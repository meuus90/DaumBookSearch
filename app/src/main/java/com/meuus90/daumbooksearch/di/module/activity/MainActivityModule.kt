package com.meuus90.daumbooksearch.di.module.activity

import com.meuus90.daumbooksearch.di.module.fragment.BookDetailFragmentModule
import com.meuus90.daumbooksearch.di.module.fragment.BookListFragmentModule
import com.meuus90.daumbooksearch.di.module.fragment.SplashFragmentModule
import com.meuus90.daumbooksearch.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
            SplashFragmentModule::class,
            BookListFragmentModule::class,
            BookDetailFragmentModule::class
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}