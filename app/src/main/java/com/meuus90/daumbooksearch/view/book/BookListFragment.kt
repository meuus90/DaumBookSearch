package com.meuus90.daumbooksearch.view.book

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.meuus90.base.constant.AppConfig
import com.meuus90.base.view.BaseActivity.Companion.BACK_STACK_STATE_ADD
import com.meuus90.base.view.BaseFragment
import com.meuus90.base.view.ext.gone
import com.meuus90.base.view.ext.hideKeyboard
import com.meuus90.base.view.ext.show
import com.meuus90.base.view.util.DetailsTransition
import com.meuus90.base.view.util.SnappingLinearLayoutManager
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.model.paging.book.BooksPageKeyedMediator
import com.meuus90.daumbooksearch.model.schema.book.BookRequest
import com.meuus90.daumbooksearch.view.MainActivity
import com.meuus90.daumbooksearch.view.book.BookDetailFragment.Companion.KEY_BOOK
import com.meuus90.daumbooksearch.view.book.adapter.BookListAdapter
import com.meuus90.daumbooksearch.viewmodel.book.BooksViewModel
import kotlinx.android.synthetic.main.fragment_book_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class BookListFragment : BaseFragment() {
    @Inject
    internal lateinit var bookViewModel: BooksViewModel

    private val mainActivity: MainActivity by lazy {
        activity as MainActivity
    }

    val searchSchema = BookRequest("", null, null, AppConfig.remotePagingSize, 0)

    private var createdView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.e("BookListFragment onCreateView")

        if (createdView == null) createdView =
            inflater.inflate(R.layout.fragment_book_list, container, false)
        return createdView
    }

    private var isRecyclerViewInitialized = false
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.e("BookListFragment onActivityCreated")

        initViewsListener()

        if (!isRecyclerViewInitialized) {
            initViews()
            initAdapter()

//            updateBooks()
            showErrorView(
                R.drawable.brand_wearefriends3,
                getString(R.string.network_message_welcome_title),
                getString(R.string.network_message_welcome_message)
            )

            isRecyclerViewInitialized = true
        }
    }

    private val adapter = BookListAdapter { item, sharedView ->
        val bundle = Bundle()
        bundle.putParcelable(KEY_BOOK, item)

        val newFragment = addFragment(
            BookDetailFragment::class.java,
            BACK_STACK_STATE_ADD,
            bundle,
            sharedView,
            false
        )

        newFragment.sharedElementEnterTransition = DetailsTransition()
        newFragment.enterTransition = null
        exitTransition = null
        newFragment.sharedElementReturnTransition = DetailsTransition()
    }

    private fun initAdapter() {
        recyclerView.adapter = adapter

        recyclerView.setItemViewCacheSize(AppConfig.recyclerViewCacheSize)
        recyclerView.setHasFixedSize(false)
        recyclerView.isVerticalScrollBarEnabled = false

        val layoutManager = SnappingLinearLayoutManager(context, RecyclerView.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        recyclerView.layoutManager = layoutManager

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            adapter.loadStateFlow
                .collectLatest { loadStates ->
                    swipeRefreshLayout.isRefreshing =
                        loadStates.refresh is LoadState.Loading && adapter.itemCount < 1
//                recyclerView.scrollToPosition(0)
                }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            bookViewModel.books
//                .distinctUntilChangedBy {
//                    Timber.e("BookListFragment pagingData distinctUntilChangedBy hash ${it.hashCode()}")
//                    it.hashCode()
//                }
                .collectLatest {
                    recyclerView.show()
                    v_error.gone()
                    adapter.submitData(lifecycle, it)
                    Timber.e("BookListFragment distinctUntilChangedBy adapter.submitData")
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

    private fun initViews() {
        val sortItems = resources.getStringArray(R.array.doc_sort)
        spinner_sort.adapter =
            ArrayAdapter(context, R.layout.item_spinner, sortItems)

        val targetItems = resources.getStringArray(R.array.search_target)
        spinner_target.adapter =
            ArrayAdapter(context, R.layout.item_spinner, targetItems)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewsListener() {
        iv_home.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        iv_search.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
            updateBooks()
        }

        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                updateBooks()
                true
            } else {
                false
            }
        }

        et_search.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateBooks()
                true
            } else {
                false
            }
        }

        spinner_sort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // sort param is not working for kakao api

                val isChanged = searchSchema.setSortType(position)

                if (isChanged)
                    updateBooks()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spinner_target.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val isChanged = searchSchema.setSearchTarget(position)

                if (isChanged)
                    updateBooks()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE)
                et_search.hideKeyboard()
            false
        }

        et_search.setOnClickListener {
            appbar_search.setExpanded(true)
        }

        swipeRefreshLayout.setOnRefreshListener {
            updateBooks(++searchSchema.refreshCount)
        }

        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    updateBooksByDebounce()
                }
            }
        })
    }

    private var initialized = false
    private fun updateErrorUI(state: LoadState.Error) {
        val error = state.error
        if (error is BooksPageKeyedMediator.EmptyResultException) {
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

//            Timber.e(error)
            Timber.e(bodyStr)

            if (networkError.isMissingParameter()) {
                if (!initialized) {
                    showErrorView(
                        R.drawable.brand_wearefriends3,
                        getString(R.string.network_message_welcome_title),
                        getString(R.string.network_message_welcome_message)
                    )
                    initialized = true
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

    private fun updateBooksByDebounce() {
        Timber.e("BookListFragment updatedBooksFromInput")

        et_search.text?.trim().toString().let {
            searchSchema.query = it
            bookViewModel.postBookSchemaWithDebounce(searchSchema)
        }
    }

    private fun updateBooks(refreshCount: Int = 0) {
        Timber.e("BookListFragment updatedBooksFromInput")
        hideKeyboard()

        et_search.text?.trim().toString().let {
            searchSchema.refreshCount = refreshCount
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