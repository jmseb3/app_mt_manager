package com.wonddak.mtmanger.model

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<out T>(val data:T?) : Resource<T>()
}
