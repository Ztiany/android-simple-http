package com.android.sdk.net.coroutines.nullable

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.RetryDeterminer
import com.android.sdk.net.coroutines.apiCallInternal
import timber.log.Timber

internal suspend fun <T : Any?> internalApiCallNullable(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    call: suspend () -> Result<T>?,
): CallResult<T?> {
    return apiCallInternal(hostFlag, false, call)
}

internal suspend fun <T : Any?> internalApiCallRetryNullable(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T>?,
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

internal suspend fun <T : Any?> internalExecuteApiCallNullable(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    call: suspend () -> Result<T>?,
): T? {
    when (val result = internalApiCallNullable(hostFlag, call)) {
        is CallResult.Success -> {
            return result.data
        }

        is CallResult.Error -> {
            throw result.error
        }
    }
}

internal suspend fun <T : Any?> internalExecuteApiCallNullable(
    hostFlag: String = NetContext.DEFAULT_CONFIG,
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T>?,
): T? {
    when (val result = internalApiCallRetryNullable(hostFlag, retryDeterminer, call)) {
        is CallResult.Success -> {
            return result.data
        }

        is CallResult.Error -> {
            throw result.error
        }
    }
}