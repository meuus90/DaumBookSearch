package com.meuus90.daumbooksearch.presentation.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.meuus90.base.utility.Params
import com.meuus90.base.utility.Query
import com.meuus90.base.utility.network.Status
import com.meuus90.base.view.AutoClearedValue
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel
import com.meuus90.daumbooksearch.presentation.BaseFragment
import com.meuus90.daumbooksearch.presentation.book.adapter.BookListAdapter
import kotlinx.android.synthetic.main.fragment_book_list.*
import javax.inject.Inject

class BookListFragment : BaseFragment() {
    companion object {
        fun newInstance() = BookListFragment().apply {
            arguments = Bundle(1).apply {
                putString(FRAGMENT_TAG, BookListFragment::class.java.name)
            }
        }
    }

    @Inject
    internal lateinit var bookViewModel: BookViewModel

    private lateinit var adapter: BookListAdapter

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

        adapter = BookListAdapter { item ->
            //todo : goto detail page
        }
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(false)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.itemAnimator?.changeDuration = 0
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        recyclerView.isVerticalScrollBarEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        swipeRefreshLayout.setOnRefreshListener {
            getPlaylist()
        }

        getPlaylist()
    }

    private fun getPlaylist() {
        val schema = BookSchema("test", "accuracy", "title", 50, 1)
        val query = Query().init(schema)

        bookViewModel.pullTrigger(Params(query))
        bookViewModel.book.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.getStatus()) {
                Status.SUCCESS -> {
                    swipeRefreshLayout.isRefreshing = false
                }

                Status.LOADING -> {
                }

                Status.ERROR -> {
                    swipeRefreshLayout.isRefreshing = false
                }

                else -> {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })
        bookViewModel.livePagedList.observe(viewLifecycleOwner, Observer { pagedList ->
            adapter.submitList(pagedList)
        })
    }
}