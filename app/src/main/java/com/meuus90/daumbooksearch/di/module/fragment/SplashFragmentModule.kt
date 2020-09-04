package com.meuus90.daumbooksearch.di.module.fragment

import com.meuus90.daumbooksearch.ui.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SplashFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeSplashFragment(): SplashFragment
}