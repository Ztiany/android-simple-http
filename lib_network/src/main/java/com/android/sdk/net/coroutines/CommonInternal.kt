package com.android.sdk.net.coroutines

import com.android.sdk.net.HostConfigProvider
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.result.Result
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException


internal fun createApiException(
    result: Result<*>,
    hostFlag: String,
    hostConfigProvider: HostConfigProvider,
): Throwable {

    var checkedExceptionFactory = hostConfigProvider.exceptionFactory()

    if (checkedExceptionFactory == null) {
        checkedExceptionFactory = hostConfigProvider.exceptionFactory()
    }

    if (checkedExceptionFactory != null) {
        val exception = checkedExceptionFactory.create(result, hostFlag)
        if (exception != null) {
            return exception
        }
    }

    return ApiErrorException(result.code, result.message, hostFlag)
}

internal fun postAction(hostFlag: String): CoroutinesResultPostProcessor {
    return NetContext.get().hostConfigProvider(hostFlag).coroutinesResultPostProcessor() ?: EMPTY_ENTRY
}

private val EMPTY_ENTRY = object : CoroutinesResultPostProcessor {
    override suspend fun retry(throwable: Throwable): Boolean {
        return false
    }
}

internal fun transformHttpException(hostFlag: String, throwable: Throwable): Throwable {
    // Catch CancellationException will cause coroutines unable to be cancelled.
    if (throwable is CancellationException) {
        throw throwable
    }

    val errorBodyHandler = NetContext.get().hostConfigProvider(hostFlag).errorBodyHandler()

    return if (errorBodyHandler != null && throwable is HttpException && throwable.code() < 500/*http status code*/) {
        val errorBody = throwable.response()?.errorBody()
        if (errorBody == null) {
            throwable
        } else {
            errorBodyHandler.parseErrorBody(errorBody.string(), hostFlag) ?: throwable
        }
    } else {
        throwable
    }
}