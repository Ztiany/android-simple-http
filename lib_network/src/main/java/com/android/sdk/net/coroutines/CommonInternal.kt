package com.android.sdk.net.coroutines

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.result.Result
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

internal fun createApiException(
    result: Result<*>,
    hostFlag: String,
): ApiErrorException {
    return ApiErrorException(
        result.code,
        result.message,
        result,
        hostFlag
    )
}

internal fun postAction(hostFlag: String): CoroutinesResultPostProcessor {
    return NetContext.get().hostConfig(hostFlag).coroutinesResultPostProcessor() ?: EMPTY_ENTRY
}

private val EMPTY_ENTRY = object : CoroutinesResultPostProcessor {
    override suspend fun retry(throwable: Throwable): Boolean {
        return false
    }
}

fun transformHttpException(hostFlag: String, throwable: Throwable): Throwable {
    // Catch CancellationException will cause coroutines unable to be cancelled.
    if (throwable is CancellationException) {
        throw throwable
    }

    val httpExceptionHandler = NetContext.get().hostConfig(hostFlag).errorBodyHandler()

    return if (httpExceptionHandler != null && throwable is HttpException && throwable.code() < 500/*http status code*/) {
        httpExceptionHandler.handleException(throwable, hostFlag) ?: throwable
    } else {
        throwable
    }
}