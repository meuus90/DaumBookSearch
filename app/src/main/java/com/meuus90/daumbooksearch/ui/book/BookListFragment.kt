package com.meuus90.daumbooksearch.ui.book

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.Fade
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meuus90.base.constant.AppConfig
import com.meuus90.base.view.BaseActivity.Companion.BACK_STACK_STATE_ADD
import com.meuus90.base.view.BaseFragment
import com.meuus90.base.view.ext.gone
import com.meuus90.base.view.ext.show
import com.meuus90.base.view.util.AutoClearedValue
import com.meuus90.base.view.util.DetailsTransition
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.domain.viewmodel.book.TestBookViewModel
import com.meuus90.daumbooksearch.presentation.MainActivity
import com.meuus90.daumbooksearch.presentation.book.BookDetailFragment.Companion.KEY_BOOK
import com.meuus90.daumbooksearch.presentation.book.adapter.BookListAdapter
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel.Companion.CALL_DEBOUNCE
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel.Companion.CALL_DIRECTLY
import com.meuus90.daumbooksearch.ui.MainActivity
import com.meuus90.daumbooksearch.ui.book.BookDetailFragment.Companion.KEY_BOOK
import com.meuus90.daumbooksearch.ui.book.adapter.BookListAdapter
import kotlinx.android.synthetic.main.fragment_book_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

class BookListFragment : BaseFragment() {
    @Inject
    internal lateinit var bookViewModel: TestBookViewModel

    private lateinit var adapter: BookListAdapter

    val searchSchema = BookSchema("", null, null, AppConfig.pagedListSize, 1)

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

        adapter = BookListAdapter { item, sharedView ->
            val bundle = Bundle()
            bundle.putParcelable(KEY_BOOK, item)

            val newFragment = addFragment(
                BookDetailFragment::class.java,
                BACK_STACK_STATE_ADD,
                bundle,
                sharedView
            )

            newFragment.sharedElementEnterTransition = DetailsTransition()
            newFragment.enterTransition = Fade()
            exitTransition = Fade()
            newFragment.sharedElementReturnTransition = DetailsTransition()
        }
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.setItemViewCacheSize(20)
        recyclerView.isVerticalScrollBarEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        swipeRefreshLayout.setOnRefreshListener {
            updatedSubredditFromInput()
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
                val isChanged = searchSchema.setSortType(position)

                if (isSortSpinnerInitialized) {
                    if (isChanged)
                        updatedSubredditFromInput()
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
                val isChanged = searchSchema.setSearchTarget(position)

                if (isTargetSpinnerInitialized) {
                    if (isChanged)
                        updatedSubredditFromInput()
                } else {
                    isTargetSpinnerInitialized = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        iv_home.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        iv_search.setOnClickListener {
            updatedSubredditFromInput()
        }

        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updatedSubredditFromInput()
                true
            } else {
                false
            }
        }

        et_search.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatedSubredditFromInput()
                true
            } else {
                false
            }
        }

        initAdapter()
    }

    private fun initAdapter() {
        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            adapter.loadStateFlow.collectLatest { loadStates ->
                swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            bookViewModel.posts.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(FlowPreview::class)
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter {
                    it.refresh is LoadState.NotLoading
                }
                .collect {
                    recyclerView.scrollToPosition(0)
                }
        }
    }

    private fun updatedSubredditFromInput() {
        et_search.text?.trim().toString().let {
            if (it.isNotBlank() && bookViewModel.shouldShowSubreddit(searchSchema)) {
                bookViewModel.showSubreddit(searchSchema)
            }
        }
    }

    private fun showErrorView(title: String, message: String) {
        recyclerView.gone()
        v_error.show()
        tv_error_title.text = title
        tv_error_message.text = message
    }
}