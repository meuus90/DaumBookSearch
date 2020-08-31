
package com.meuus90.base.network

data class NetworkError(val error: ErrorBody) {
    data class ErrorBody(val code: String?, val message: String)
}