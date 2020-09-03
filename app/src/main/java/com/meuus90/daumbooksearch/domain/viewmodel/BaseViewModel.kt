package com.meuus90.daumbooksearch.domain.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<in P, T> : ViewModel() {
    abstract fun pullTrigger(params: P)
}