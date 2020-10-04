package com.meuus90.daumbooksearch

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.bumptech.glide.Glide
import com.meuus90.daumbooksearch.di.helper.AppInjector
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.exitProcess


class DaumBookSearch : Application(), LifecycleObserver, HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    internal var isInForeground = false

    companion object {
        fun exitApplication(activity: Activity) {
            ActivityCompat.finishAffinity(activity)
            exit()
        }

        private fun exit() {
            android.os.Process.killProcess(android.os.Process.myPid())
            System.runFinalizersOnExit(true)
            exitProcess(0)
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.uprootAll()
            Timber.plant(Timber.DebugTree())
        }

        Logger.addLogAdapter(AndroidLogAdapter())

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        registerComponentCallbacks(DaumBookSearchComponentCallback(this))
        AppInjector.init(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    override fun androidInjector() = dispatchingAndroidInjector

    class DaumBookSearchComponentCallback(private val app: DaumBookSearch) : ComponentCallbacks2 {
        override fun onConfigurationChanged(newConfig: Configuration?) {}

        override fun onTrimMemory(level: Int) {
            Glide.get(app).trimMemory(level)
            if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
                app.isInForeground = true
            }
        }

        override fun onLowMemory() {
            Timber.d("onLowMemory")

            Glide.get(app).clearMemory()
        }
    }
}