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
import com.meuus90.base.view.gone
import com.meuus90.base.view.show
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel.Companion.CALL_DEBOUNCE
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel.Companion.CALL_DIRECTLY
import com.meuus90.daumbooksearch.presentation.BaseActivity.Companion.BACK_STACK_STATE_ADD
import com.meuus90.daumbooksearch.presentation.BaseFragment
import com.meuus90.daumbooksearch.presentation.book.BookDetailFragment.Companion.KEY_BOOK
import com.meuus90.daumbooksearch.presentation.book.adapter.BookListAdapter
import kotlinx.android.synthetic.main.fragment_book_list.*
import javax.inject.Inject

class BookListFragment : BaseFragment() {
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

//    override fun onResume() {
//        super.onResume()
//
//        recyclerView.refreshDrawableState()
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = BookListAdapter { item, sharedView ->
            val bundle = Bundle()
            bundle.putParcelable(KEY_BOOK, item)

            addFragment(BookDetailFragment::class.java, BACK_STACK_STATE_ADD, bundle, sharedView)
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
                val before = searchSchema.sort
                searchSchema.setSortType(position)
                val after = searchSchema.sort

                if (isSortSpinnerInitialized) {
                    if (before != after)
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
                val before = searchSchema.target
                searchSchema.setSearchTarget(position)
                val after = searchSchema.target

                if (isTargetSpinnerInitialized) {
                    if (before != after)
                        getPlaylist(CALL_DIRECTLY)
                } else {
                    isTargetSpinnerInitialized = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        et_search.addAfterTextChangedListener { query ->
            val before = searchSchema.query
            searchSchema.setQueryStr(query)

            if (query == "")
                showErrorView(
                    getString(R.string.network_message_no_item_title, query),
                    getString(R.string.network_message_no_item_message)
                )
            else if (before != query)
                getPlaylist(CALL_DEBOUNCE)
        }

        iv_home.setOnClickListener {
            recyclerView.smoothScrollBy(0, 0)
        }

        iv_search.setOnClickListener {
            val query = et_search.text.toString()
            searchSchema.setQueryStr(query)

            if (query == "")
                showErrorView(
                    getString(R.string.network_message_no_item_title, query),
                    getString(R.string.network_message_no_item_message)
                )
            else
                getPlaylist(CALL_DEBOUNCE)
        }

//        getPlaylist(CALL_DIRECTLY)
        showErrorView(getString(R.string.network_message_welcome_title), "")
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
                    showErrorView(
                        getString(R.string.network_message_error_title),
                        getString(R.string.network_message_error_message)
                    )
                }

                else -> {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })
        bookViewModel.livePagedList.observe(viewLifecycleOwner, Observer { pagedList ->
            if (pagedList.isNotEmpty()) {
                recyclerView.show()
                adapter.submitList(pagedList)
            } else {
                val schema = query.datas[0] as BookSchema
                showErrorView(
                    getString(R.string.network_message_no_item_title, schema.query),
                    getString(R.string.network_message_no_item_message)
                )
            }
        })
    }

    private fun showErrorView(title: String, message: String) {
        recyclerView.gone()
        v_error.show()
        tv_error_title.text = title
        tv_error_message.text = message
    }
}