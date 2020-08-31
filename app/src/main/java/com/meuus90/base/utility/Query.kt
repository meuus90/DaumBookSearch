package com.meuus90.base.utility

import com.meuus90.base.network.NetworkBoundResource
import javax.inject.Singleton

@Singleton
class Query {
    var params: List<Any> = listOf()
    var boundType: Int = 0

    companion object {
        fun query(params: List<Any>) =
            Query().apply {
                this.params = params
                boundType = NetworkBoundResource.BOUND_FROM_BACKEND
            }
    }
}