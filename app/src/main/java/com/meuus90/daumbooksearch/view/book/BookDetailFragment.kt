package com.meuus90.daumbooksearch.view.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.meuus90.base.common.util.NumberTools
import com.meuus90.base.common.util.TimeTools
import com.meuus90.base.view.BaseFragment
import com.meuus90.base.view.ext.gone
import com.meuus90.base.view.util.AutoClearedValue
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.model.schema.book.BookDoc
import com.meuus90.daumbooksearch.view.Caller
import com.meuus90.daumbooksearch.view.MainActivity
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
            .error(R.drawable.no_image)
            .into(iv_thumbnail)

        iv_thumbnail.transitionName = bookDoc.position.toString()

        tv_title.text = bookDoc.title

        tv_author_title.text = getString(R.string.author_format, bookDoc.authors.joinToString())

        tv_author.text = bookDoc.authors.joinToString()

        if (bookDoc.translators.isNotEmpty())
            tv_translators.text = bookDoc.translators.joinToString()
        else
            v_translators.gone()

        tv_publisher.text = bookDoc.publisher
        tv_date.text =
            TimeTools.convertDateFormat(bookDoc.datetime, TimeTools.ISO8601, TimeTools.YMD)
        tv_price_original.text = NumberTools.convertToString(bookDoc.price)
        tv_price_sale.text = NumberTools.convertToString(bookDoc.sale_price)
        tv_status.text = bookDoc.status
        tv_contents.text = getString(R.string.contents_format, bookDoc.contents)

        tv_next.setOnClickListener {
            val url = bookDoc.url
            Caller.openUrlLink(context, url)
        }
        iv_home.setOnClickListener {
            mainActivity.onBackPressed()
        }
    }
}