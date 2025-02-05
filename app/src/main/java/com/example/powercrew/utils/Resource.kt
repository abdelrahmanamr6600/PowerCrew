package com.example.powercrew.utils

//sealed class Resource<out T> {
//    data object Loading : Resource<Nothing>()
//    data class Success<out T>(val data: T) : Resource<T>()
//    data class Failure(val exception: Exception) : Resource<Nothing>()
//}


sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}