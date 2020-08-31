package com.meuus90.base.utility

import javax.inject.Singleton

@Singleton
class Query {
    companion object {
        const val BOUND_FROM_BACKEND = 0
        const val BOUND_FROM_LOCAL = 0
    }

    var params: List<Any?> = listOf()
    var boundType: Int = BOUND_FROM_BACKEND

    fun setParams(vararg params: Any?): Query {
        this.params = params.asList()
        return this
    }

    fun setType(boundType: Int): Query {
        this.boundType = boundType
        return this
    }
}