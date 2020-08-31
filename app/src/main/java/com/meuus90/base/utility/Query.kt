package com.meuus90.base.utility

import com.meuus90.base.network.NetworkBoundResource
import javax.inject.Singleton

@Singleton
class Query {
    var params: List<Any?> = listOf()
    var boundType: Int = NetworkBoundResource.BOUND_FROM_BACKEND

    companion object {
        fun setParams(vararg params: Any?) =
            Query().apply {
                this.params = params.asList()
            }
    }
}