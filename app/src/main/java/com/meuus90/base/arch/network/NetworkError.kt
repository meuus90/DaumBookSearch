package com.meuus90.base.arch.network

data class NetworkError(val errorType: String?, val message: String = ERROR_DEFAULT) {

    fun isMissingParameter() = errorType == TYPE_MISSING_PARAMETER

    companion object {
        const val ERROR_DEFAULT = "An unexpected error has occurred"
        const val ERROR_SERVICE_UNAVAILABLE = 503


        const val TYPE_MISSING_PARAMETER = "MissingParameter"
    }
}