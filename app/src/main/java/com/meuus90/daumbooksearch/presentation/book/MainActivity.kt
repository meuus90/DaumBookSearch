package com.meuus90.daumbooksearch.presentation.book

import android.os.Bundle
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.presentation.BaseActivity

class MainActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFragment(
            BookListFragment::class.java
        )
    }
}