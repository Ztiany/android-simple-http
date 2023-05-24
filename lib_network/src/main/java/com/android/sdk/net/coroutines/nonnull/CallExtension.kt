package com.android.sdk.net.coroutines.nonnull

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.*
import timber.log.Timber

suspend fun <T : Any> apiCall(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    call: suspend () -> Result<T>
): CallResult<T> {
    return apiCallInternal(hostFlag, true, call)
}

suspend fun <T : Any> apiCallRetry(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T>,
): CallResult<T> {

    var result = apiCallInternal(hostFlag, true, call)

    var count = 0

    while (true) {
        if (result is CallResult.Error && retryDeterminer(++count, result.error)) {
            Timber.d("executeApiCallRetry at ${++count}")
            result = apiCallInternal(hostFlag, true, call)
        } else {
            return result
        }
    }
}

suspend fun <T : Any> executeApiCall(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    call: suspend () -> Result<T>
): T {

    when (val result = apiCall(hostFlag, call)) {
        is CallResult.Success -> {
            return result.data
        }

        is CallResult.Error -> {
            throw result.error
        }
    }
}

suspend fun <T : Any> executeApiCallRetry(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T>
): T {

    when (val result = apiCallRetry(hostFlag, retryDeterminer, call)) {
        is CallResult.Success -> {
            return result.data
        }

        is CallResult.Error -> {
            throw result.error
        }
    }

}