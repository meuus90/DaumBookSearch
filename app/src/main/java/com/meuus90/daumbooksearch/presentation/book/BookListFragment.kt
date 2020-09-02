package com.meuus90.daumbooksearch.presentation.book

import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meuus90.base.arch.Params
import com.meuus90.base.arch.Query
import com.meuus90.base.arch.network.Status
import com.meuus90.base.view.BaseActivity.Companion.BACK_STACK_STATE_ADD
import com.meuus90.base.view.BaseFragment
import com.meuus90.base.view.ext.addAfterTextChangedListener
import com.meuus90.base.view.ext.gone
import com.meuus90.base.view.ext.show
import com.meuus90.base.view.util.AutoClearedValue
import com.meuus90.base.view.util.DetailsTransition
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.data.model.book.BookDoc
import com.meuus90.daumbooksearch.data.model.book.BookResponseModel
import com.meuus90.daumbooksearch.data.model.book.BookSchema
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel.Companion.CALL_DEBOUNCE
import com.meuus90.daumbooksearch.domain.viewmodel.book.BookViewModel.Companion.CALL_DIRECTLY
import com.meuus90.daumbooksearch.presentation.MainActivity
import com.meuus90.daumbooksearch.presentation.book.BookDetailFragment.Companion.KEY_BOOK
import com.meuus90.daumbooksearch.presentation.book.adapter.BookListAdapter
import kotlinx.android.synthetic.main.fragment_book_list.*
import javax.inject.Inject

class BookListFragment : BaseFragment() {
    companion object {
        const val KEY_DATA = "KEY_DATA"
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
//        recyclerView.setHasFixedSize(false)
        recyclerView.setItemViewCacheSize(20)
//        recyclerView.itemAnimator?.changeDuration = 0
//        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
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
                val isChanged = searchSchema.setSortType(position)

                if (isSortSpinnerInitialized) {
                    if (isChanged)
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
                val isChanged = searchSchema.setSearchTarget(position)

                if (isTargetSpinnerInitialized) {
                    if (isChanged)
                        getPlaylist(CALL_DIRECTLY)
                } else {
                    isTargetSpinnerInitialized = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        et_search.addAfterTextChangedListener { query ->
            if (searchSchema.setQueryStr(query))
                getPlaylist(CALL_DEBOUNCE)
            else
                adapter.submitList(pagedList)
            // todo 한글 에러
            // todo Pagedlist -> PagingData
        }

        iv_home.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        iv_search.setOnClickListener {
            val query = et_search.text.toString()
            searchSchema.setQueryStr(query)

            getPlaylist(CALL_DEBOUNCE)
        }

        if (!isInitialized) {
            isInitialized = true
            showErrorView(getString(R.string.network_message_welcome_title), "")
        }
    }

    private var isInitialized = false

    private fun getPlaylist(delayType: Int) {
        val query = Query().init(searchSchema, delayType)

        bookViewModel.pullTrigger(Params(query))
        bookViewModel.book.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.getStatus()) {
                Status.SUCCESS -> {
                    swipeRefreshLayout.isRefreshing = false
                    val data = resource.getData() as BookResponseModel

                    if (data.meta.total_count > 0)
                        v_error.gone()
                }

                Status.LOADING -> {
                }

                Status.ERROR -> {
                    swipeRefreshLayout.isRefreshing = false
                    val networkError = parseToNetworkError(resource.getMessage())
                    if (networkError.errorType == "MissingParameter") {
                        val schema = query.datas[0] as BookSchema
                        showErrorView(
                            getString(R.string.network_message_no_item_title, schema.query),
                            getString(R.string.network_message_no_item_message)
                        )
                    } else {
                        showErrorView(
                            resource.getMessage() ?: "",
                            getString(R.string.network_message_error_message)
                        )
                    }
                }

                else -> {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })
        bookViewModel.livePagedList.observe(viewLifecycleOwner, Observer { pagedList ->
            adapter.submitList(pagedList)
            this.pagedList = pagedList

            if (pagedList.isNotEmpty()) {
                recyclerView.show()
                v_error.gone()
            }
        })
    }

    var pagedList: PagedList<BookDoc>? = null
    private fun showErrorView(title: String, message: String) {
        recyclerView.gone()
        v_error.show()
        tv_error_title.text = title
        tv_error_message.text = message
    }
}