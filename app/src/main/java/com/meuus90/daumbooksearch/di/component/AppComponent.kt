package com.meuus90.daumbooksearch.di.component

import android.app.Application
import com.meuus90.daumbooksearch.DaumBookSearch
import com.meuus90.daumbooksearch.di.module.AppModule
import com.meuus90.daumbooksearch.di.module.activity.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        MainActivityModule::class
    ]
)

interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

    fun inject(app: DaumBookSearch)
}