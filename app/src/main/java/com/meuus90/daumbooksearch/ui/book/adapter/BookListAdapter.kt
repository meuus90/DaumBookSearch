package com.meuus90.daumbooksearch.ui.book.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.meuus90.base.util.NumberTools
import com.meuus90.base.util.TimeTools
import com.meuus90.base.util.TimeTools.Companion.ISO8601
import com.meuus90.base.util.TimeTools.Companion.YMD
import com.meuus90.base.view.util.BaseViewHolder
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_book.view.*

class BookListAdapter(val doOnClick: (item: BookDoc, sharedView: View) -> Unit) :
    PagingDataAdapter<BookDoc, BaseViewHolder<BookDoc>>(DIFF_CALLBACK) {
    companion object {
        private val PAYLOAD_TITLE = Any()

        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<BookDoc>() {
                override fun areItemsTheSame(oldItem: BookDoc, newItem: BookDoc): Boolean =
                    oldItem.isbn == newItem.isbn

                override fun areContentsTheSame(oldItem: BookDoc, newItem: BookDoc): Boolean =
                    oldItem == newItem

                override fun getChangePayload(oldItem: BookDoc, newItem: BookDoc): Any? {
                    return if (sameExceptTitle(oldItem, newItem)) PAYLOAD_TITLE
                    else null
                }
            }

        private fun sameExceptTitle(
            oldItem: BookDoc,
            newItem: BookDoc
        ): Boolean {
            return oldItem.copy(isbn = newItem.isbn) == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BookDoc> {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val view = inflater.inflate(R.layout.item_book, parent, false)
        return BookItemHolder(view, this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BookDoc>, position: Int) {
        getItem(position)?.let { item ->
            holder.bindItemHolder(holder, item, position)
        }
    }

    class BookItemHolder(
        override val containerView: View,
        private val adapter: BookListAdapter
    ) : BaseViewHolder<BookDoc>(containerView), LayoutContainer {
        override fun bindItemHolder(
            holder: BaseViewHolder<BookDoc>,
            item: BookDoc,
            position: Int
        ) {
            containerView.apply {
                Glide.with(context).asDrawable().clone()
                    .load(item.thumbnail)
                    .centerCrop()
                    .dontAnimate()
//                    .override(87, 123)
                    .error(R.drawable.no_image)
                    .into(iv_thumbnail)

                iv_thumbnail.transitionName = position.toString()

                tv_title.text = item.title

                tv_author.text = item.authors.joinToString()
                tv_publisher.text = item.publisher
                tv_date.text = TimeTools.convertDateFormat(item.datetime, ISO8601, YMD)
                tv_price.text = NumberTools.convertToString(item.price)
                tv_status.text = item.status

                v_root.setOnClickListener {

                    item.position = position
                    adapter.doOnClick(item, iv_thumbnail)
                }
            }
        }

        override fun onItemSelected() {
            containerView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            containerView.setBackgroundColor(0)
        }
    }
}