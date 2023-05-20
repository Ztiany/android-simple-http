package com.android.sdk.net.coroutines

import com.android.sdk.net.HostConfigProvider
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.core.result.Result
import retrofit2.HttpException
import timber.log.Timber

internal const val RETRY_TIMES = 3
internal const val RETRY_DELAY = 3000L

internal fun createApiException(
    result: Result<*>,
    exceptionFactory: ExceptionFactory? = null,
    hostFlag: String,
    netProvider: HostConfigProvider
): Throwable {

    var checkedExceptionFactory = exceptionFactory

    if (checkedExceptionFactory == null) {
        checkedExceptionFactory = netProvider.exceptionFactory()
    }

    if (checkedExceptionFactory != null) {
        val exception = checkedExceptionFactory.create(result, hostFlag)
        if (exception != null) {
            return exception
        }
    }

    return ApiErrorException(result.code, result.message, hostFlag)
}

internal fun retryPostAction(): CoroutinesResultPostProcessor {
    val commonProvider = NetContext.get().commonProvider()
    val coroutinesResultPostProcessor = commonProvider.coroutinesResultPostProcessor()

    if (coroutinesResultPostProcessor != null) {
        return coroutinesResultPostProcessor
    }

    return EMPTY_ENTRY
}

private val EMPTY_ENTRY = object : CoroutinesResultPostProcessor {
    override suspend fun retry(throwable: Throwable): Boolean {
        return false
    }
}

internal fun transformHttpException(
    throwable: Throwable,
    errorBodyParser: ErrorBodyParser?,
): Throwable {
    Timber.e(throwable, "transformHttpException")
    val errorBodyHandler = errorBodyParser ?: NetContext.get().commonProvider().errorBodyHandler()

    return if (errorBodyHandler != null && throwable is HttpException && throwable.code() < 500/*http status code*/) {
        val errorBody = throwable.response()?.errorBody()
        if (errorBody == null) {
            throwable
        } else {
            errorBodyHandler.parseErrorBody(errorBody.string()) ?: throwable
        }
    } else {
        throwable
    }
}

