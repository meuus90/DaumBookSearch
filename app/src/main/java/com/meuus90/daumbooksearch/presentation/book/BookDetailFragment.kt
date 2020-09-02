package com.meuus90.daumbooksearch.presentation.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.meuus90.base.view.AutoClearedValue
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_book_detail.*

class BookDetailFragment : BaseFragment() {
    companion object {
        const val KEY_BOOK = "KEY_BOOK"
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
                inflater.inflate(R.layout.fragment_book_detail, container, false)
            )
        return acvView.get()?.rootView
    }

    lateinit var bookDoc: BookDoc
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = arguments
        bookDoc = bundle?.getParcelable<BookDoc>(KEY_BOOK)!!

        Glide.with(context).asDrawable().clone()
            .load(bookDoc.thumbnail)
            .centerCrop()
            .dontAnimate()
            .error(R.drawable.ic_b)
            .into(iv_thumbnail)

        iv_thumbnail.transitionName = bookDoc.toString()
    }
}