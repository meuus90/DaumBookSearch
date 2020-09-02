package com.meuus90.daumbooksearch.presentation.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.meuus90.base.arch.Params
import com.meuus90.base.arch.Query
import com.meuus90.base.arch.network.Status
import com.meuus90.base.view.AutoClearedValue
import com.meuus90.base.view.addAfterTextChangedListener
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel.Companion.CALL_DEBOUNCE
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel.Companion.CALL_DIRECTLY
import com.meuus90.daumbooksearch.presentation.BaseActivity.Companion.BACK_STACK_STATE_ADD
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

    val searchSchema = BookSchema("", null, null, 50, 1)

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
            addFragment(BookDetailFragment::class.java, BACK_STACK_STATE_ADD)
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
            getPlaylist(CALL_DIRECTLY)
        }

        var isSortSpinnerInitialized = false
        val sortItems = resources.getStringArray(R.array.doc_sort)
        spinner_sort.adapter =
            ArrayAdapter(context, R.layout.item_spinner, sortItems)
        spinner_sort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                searchSchema.setSortType(position)

                if (isSortSpinnerInitialized) {
                    getPlaylist(CALL_DIRECTLY)
                } else {
                    isSortSpinnerInitialized = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        var isTargetSpinnerInitialized = false
        val targetItems = resources.getStringArray(R.array.search_target)
        spinner_target.adapter =
            ArrayAdapter(context, R.layout.item_spinner, targetItems)
        spinner_target.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                searchSchema.setSearchTarget(position)

                if (isTargetSpinnerInitialized) {
                    getPlaylist(CALL_DIRECTLY)
                } else {
                    isTargetSpinnerInitialized = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        et_search.addAfterTextChangedListener { query ->
            searchSchema.setQueryStr(query)
            getPlaylist(CALL_DEBOUNCE)
        }

        getPlaylist(CALL_DIRECTLY)
    }

    private fun getPlaylist(delayType: Int) {
        val query = Query().init(searchSchema, delayType)

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