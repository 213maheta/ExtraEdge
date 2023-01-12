package com.extraedge.practical_test.sealed

import com.extraedge.practical_test.model.RocketResponse

sealed class NetworkResult{
    data class Success<T>(val data: T) : NetworkResult()
    data class Error<T>(val message: String) : NetworkResult()
    object Loading: NetworkResult()
    object Empty: NetworkResult()
}