package com.meuus90.daumbooksearch.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.meuus90.daumbooksearch.di.Injectable

open class BaseFragment : Fragment(), Injectable {
    companion object {
        const val FRAGMENT_TAG = "fragment_tag"
    }

    lateinit var baseActivity: BaseActivity
    private lateinit var context: Context

    override fun getContext() = context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        baseActivity = (context as BaseActivity)
        this.context = context
    }

    private fun getScreenName(): String {
        return this::class.java.simpleName
    }

    internal fun addFragment(
        cls: Class<*>,
        backStackState: Int,
        bundle: Bundle? = null,
        sharedView: View? = null
    ): Fragment {
        return baseActivity.addFragment(cls, backStackState, bundle, sharedView)
    }

    internal fun goToRootFragment() {
        baseActivity.goToRootFragment()
    }

    internal fun showLoading(show: Boolean) {
        baseActivity.showLoading(show)
    }
}