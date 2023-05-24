package com.android.sdk.net.coroutines.nullable

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.*
import timber.log.Timber

suspend fun <T> apiCallNullable(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    call: suspend () -> Result<T?>?
): CallResult<T?> {
    return apiCallInternal(hostFlag, false, call)
}

suspend fun <T> apiCallRetryNullable(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T?>?,
): CallResult<T?> {

    var result = apiCallInternal(hostFlag, false, call)

    var count = 0

    while (true) {
        if (result is CallResult.Error && retryDeterminer(++count, result.error)) {
            Timber.d("executeApiCallRetry at ${++count}")
            result = apiCallInternal(hostFlag, false, call)
        } else {
            return result
        }
    }
}

suspend fun <T> executeApiCallNullable(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    call: suspend () -> Result<T?>?
): T? {
    when (val result = apiCallNullable(hostFlag, call)) {
        is CallResult.Success -> {
            return result.data
        }

        is CallResult.Error -> {
            throw result.error
        }
    }
}

suspend fun <T> executeApiCallNullable(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T?>?
): T? {
    when (val result = apiCallRetryNullable(hostFlag, retryDeterminer, call)) {
        is CallResult.Success -> {
            return result.data
        }

        is CallResult.Error -> {
            throw result.error
        }
    }
}