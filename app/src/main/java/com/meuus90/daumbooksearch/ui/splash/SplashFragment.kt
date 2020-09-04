package com.meuus90.daumbooksearch.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.meuus90.base.constant.AppConfig
import com.meuus90.base.view.BaseActivity.Companion.BACK_STACK_STATE_REPLACE
import com.meuus90.base.view.BaseFragment
import com.meuus90.base.view.util.AutoClearedValue
import com.meuus90.daumbooksearch.R
import com.meuus90.daumbooksearch.ui.book.BookListFragment
import com.meuus90.daumbooksearch.viewmodel.splash.SplashViewModel
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

class SplashFragment : BaseFragment() {
    @Inject
    internal lateinit var splashViewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val acvView =
            AutoClearedValue(
                this,
                inflater.inflate(R.layout.fragment_splash, container, false)
            )
        return acvView.get()?.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        splashViewModel.clearCache()
    }

    override fun onResume() {
        super.onResume()

        Handler().postDelayed({
            addFragment(
                BookListFragment::class.java,
                BACK_STACK_STATE_REPLACE,
                null,
                iv_splash,
                false
            )
        }, AppConfig.splashDelay)

    }
}