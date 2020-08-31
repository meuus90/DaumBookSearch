package com.meuus90.daumbooksearch.presentation.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meuus90.base.view.AutoClearedValue
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.presentation.BaseFragment

class BookListFragment : BaseFragment() {
    companion object {
        fun newInstance() = BookDetailFragment().apply {
            arguments = Bundle(1).apply {
                putString(FRAGMENT_TAG, BookDetailFragment::class.java.name)
            }
        }
    }

    private val mainActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val acvView =
            AutoClearedValue(
                this,
                inflater.inflate(R.layout.fragment_book_list, container, false)
            )
        return acvView.get()?.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}