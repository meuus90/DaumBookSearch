package com.meuus90.daumbooksearch.presentation.book.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.meuus90.base.view.BaseViewHolder
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_book.view.*

class BookListAdapter(val doOnClick: (item: BookDoc) -> Unit) :
    PagedListAdapter<BookDoc, BaseViewHolder<BookDoc>>(DIFF_CALLBACK) {
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

    override fun onBindViewHolder(
        holder: BaseViewHolder<BookDoc>,
        position: Int
    ) {
        val item = getItem(position)

        item?.let {
            if (holder is BookItemHolder) {
                holder.bindItemHolder(holder, it, position)
            }
        }
    }

//    override fun getItemCount(): Int {
//        return currentList?.size ?: 0
//    }
//
//    override fun getItemId(position: Int): Long {
//        return getItem(position)?.isbn.hashCode().toLong()
//    }

    class BookItemHolder(
        override val containerView: View,
        private val adapter: BookListAdapter
    ) : BaseViewHolder<BookDoc>(containerView), LayoutContainer {
        @SuppressLint("SetTextI18n")
        override fun bindItemHolder(
            holder: BaseViewHolder<BookDoc>,
            item: BookDoc,
            position: Int
        ) {
            containerView.apply {
                tv_title.text = item.title

                tv_isbn.text = item.isbn

                v_root.setOnClickListener {
                    adapter.doOnClick(item)
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