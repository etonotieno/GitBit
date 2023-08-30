package io.devbits.gitbit.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable? = null) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}
