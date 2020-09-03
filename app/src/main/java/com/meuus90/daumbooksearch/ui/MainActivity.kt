package com.meuus90.daumbooksearch.ui

import android.os.Bundle
import com.meuus90.base.view.BaseActivity
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.domain.viewmodel.book.BooksViewModel
import com.meuus90.daumbooksearch.ui.book.BookListFragment
import javax.inject.Inject

class MainActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    @Inject
    internal lateinit var bookViewModel: BooksViewModel

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookViewModel.clearCache()

        replaceFragment(
            BookListFragment::class.java
        )
    }
}