package com.android.sdk.net.coroutines.nonnull

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.*
import timber.log.Timber

internal suspend fun <T : Any> internalApiCall(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    call: suspend () -> Result<T>,
): CallResult<T> {
    return apiCallInternal(hostFlag, true, call)
}

internal suspend fun <T : Any> internalApiCallRetry(
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

internal suspend fun <T : Any> internalExecuteApiCall(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    call: suspend () -> Result<T>,
): T {

    when (val result = internalApiCall(hostFlag, call)) {
        is CallResult.Success -> {
            return result.data
        }

        is CallResult.Error -> {
            throw result.error
        }
    }
}

internal suspend fun <T : Any> internalExecuteApiCallRetry(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T>,
): T {

    when (val result = internalApiCallRetry(hostFlag, retryDeterminer, call)) {
        is CallResult.Success -> {
            return result.data
        }

        is CallResult.Error -> {
            throw result.error
        }
    }

}