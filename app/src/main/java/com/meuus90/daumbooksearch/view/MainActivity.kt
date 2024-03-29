package com.meuus90.daumbooksearch.view

import android.os.Bundle
import com.meuus90.base.view.BaseActivity
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.view.splash.SplashFragment

class MainActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFragment(SplashFragment::class.java)
    }
}