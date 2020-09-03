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
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.meuus90.base.constant.AppConfig
import com.meuus90.base.util.customDebounce
import com.meuus90.base.view.BaseActivity.Companion.BACK_STACK_STATE_ADD
import com.meuus90.base.view.BaseFragment
import com.meuus90.base.view.ext.gone
import com.meuus90.base.view.ext.show
import com.meuus90.base.view.util.AutoClearedValue
import com.meuus90.base.view.util.DetailsTransition
import com.meuus90.base.view.util.SnappingLinearLayoutManager
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.data.paging.PageKeyedRemoteMediator
import com.meuus90.daumbooksearch.domain.viewmodel.book.BooksViewModel
import com.meuus90.daumbooksearch.ui.book.BookDetailFragment.Companion.KEY_BOOK
import com.meuus90.daumbooksearch.ui.book.adapter.BookListAdapter
import kotlinx.android.synthetic.main.fragment_book_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import retrofit2.HttpException
import javax.inject.Inject

class BookListFragment : BaseFragment() {
    @Inject
    internal lateinit var bookViewModel: BooksViewModel

    val searchSchema = BookSchema("", null, null, AppConfig.pagedListSize)

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

        initAdapter()
        initViews()
    }

    private val adapter = BookListAdapter { item, sharedView ->
        val bundle = Bundle()
        bundle.putParcelable(KEY_BOOK, item)

        position = item.position

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

    lateinit var layoutManager: SnappingLinearLayoutManager

    private var position: Int? = 0
    private var pagingData: PagingData<BookDoc>? = null
    private var isRecyclerViewInitialized = false
    private fun initAdapter() {
        recyclerView.adapter = adapter
        recyclerView.setItemViewCacheSize(AppConfig.recyclerViewCacheSize)
        recyclerView.setHasFixedSize(false)
        recyclerView.isVerticalScrollBarEnabled = false
        layoutManager = SnappingLinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        if (!isRecyclerViewInitialized) {
            updatedBooksFromInput()
            isRecyclerViewInitialized = true
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            adapter.loadStateFlow
                .collectLatest { loadStates ->
                    swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
//                recyclerView.scrollToPosition(0)
                }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            bookViewModel.books
                .distinctUntilChanged()
                .collectLatest {
                    recyclerView.show()
                    v_error.gone()
                    pagingData = it
                    adapter.submitData(lifecycle, it)
                }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            bookViewModel.books
                .distinctUntilChanged()
                .customDebounce(1000)
                .collectLatest {
                    position?.let { pos ->
                        recyclerView.itemDecorationCount
                        recyclerView.smoothScrollToPosition(pos)
                        position = null
                    }
                }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(FlowPreview::class)
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.append }
//                .filter { it.refresh is LoadState.Error }
                .collectLatest {
                    val state = it.append
                    if (state is LoadState.Error)
                        updateErrorUI(state)
                    else {
                        recyclerView.show()
                        v_error.gone()
                    }
                }
        }
    }

    private var initialized = false
    private fun updateErrorUI(state: LoadState.Error) {
        val error = state.error
        if (error is PageKeyedRemoteMediator.EmptyResultException) {
            showErrorView(
                R.drawable.brand_wearefriends4,
                getString(
                    R.string.network_message_no_item_title,
                    searchSchema.query
                ),
                getString(R.string.network_message_no_item_message)
            )
        } else if (error is HttpException) {
            val bodyStr = error.response()?.errorBody()?.string()
            val networkError = parseToNetworkError(bodyStr)

            if (networkError.isMissingParameter()) {
                if (!initialized) {
                    showErrorView(
                        R.drawable.brand_wearefriends3,
                        getString(R.string.network_message_welcome_title),
                        getString(R.string.network_message_welcome_message)
                    )
                } else {
                    showErrorView(
                        R.drawable.brand_wearefriends4,
                        getString(R.string.network_message_no_item_title, ""),
                        getString(R.string.network_message_no_item_message)
                    )
                }
            } else {
                showErrorView(
                    R.drawable.brand_wearefriends7,
                    networkError.message,
                    getString(R.string.network_message_error_message)
                )
            }
        } else {
            showErrorView(
                R.drawable.brand_wearefriends7,
                getString(R.string.network_message_error_title),
                getString(R.string.network_message_error_message)
            )

        }
    }

    private fun initViews() {
        iv_home.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        iv_search.setOnClickListener {
            updatedBooksFromInput()
        }

        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                updatedBooksFromInput()
                true
            } else {
                false
            }
        }

        et_search.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatedBooksFromInput()
                true
            } else {
                false
            }
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
                // sort param is not working for kakao api

                val isChanged = searchSchema.setSortType(position)

                if (isSortSpinnerInitialized) {
                    if (isChanged)
                        updatedBooksFromInput()
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
                        updatedBooksFromInput()
                } else {
                    isTargetSpinnerInitialized = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            updatedBooksFromInput()
        }
    }

    private fun updatedBooksFromInput() {
        hideKeyboard()

        et_search.text?.trim().toString().let {
            searchSchema.query = it
            bookViewModel.postBookSchema(searchSchema)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showErrorView(drawableResId: Int, title: String, message: String) {
        recyclerView.gone()
        v_error.show()
        iv_error.setImageDrawable(context.getDrawable(drawableResId))
        tv_error_title.text = title
        tv_error_message.text = message
    }
}