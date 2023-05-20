package com.android.sdk.net.coroutines.nonnull

import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.*
import kotlinx.coroutines.delay
import timber.log.Timber

/** TODO: If the real parameterized Type of Result is needed for more features, obtain that by reflecting. */
suspend fun <T : Any> executeApiCall(
    exceptionFactory: ExceptionFactory? = null,
    errorBodyParser: ErrorBodyParser? = null,
    call: suspend () -> Result<T>
): T {

    val retryPostAction = retryPostAction()

    var result = realCallDirectly(call, true, exceptionFactory, errorBodyParser)

    if (result is CallResult.Error && retryPostAction.retry(result.error)) {
        result = realCallDirectly(call, true, exceptionFactory, errorBodyParser)
    }

    return result
}

suspend fun <T : Any> executeApiCallRetry(
    times: Int = RETRY_TIMES,
    delay: Long = RETRY_DELAY,
    exceptionFactory: ExceptionFactory? = null,
    errorBodyParser: ErrorBodyParser? = null,
    checker: ((Throwable) -> Boolean)? = null,
    call: suspend () -> Result<T>
): T {

    var result = executeApiCall(exceptionFactory, errorBodyParser, call)
    var count = 0

    repeat(times) {

        if (result is CallResult.Error && (checker == null || checker((result as CallResult.Error).error))) {
            delay(delay)
            Timber.d("executeApiCallRetry at ${++count}")
            result = executeApiCall(exceptionFactory, errorBodyParser, call)
        } else {
            return result
        }

    }

    return result
}
