package com.android.sdk.net.coroutines

import com.android.sdk.net.HostConfig
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.json.GsonUtils
import com.android.sdk.net.core.result.Result
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException


internal fun createApiException(
    result: Result<*>,
    hostFlag: String,
    hostConfigProvider: HostConfig,
): ApiErrorException {

    var apiErrorFactory = hostConfigProvider.apiErrorFactory()

    if (apiErrorFactory == null) {
        apiErrorFactory = hostConfigProvider.apiErrorFactory()
    }

    if (apiErrorFactory != null) {
        val exception = apiErrorFactory.create(result, hostFlag)
        if (exception != null) {
            return exception
        }
    }

    return ApiErrorException(result.code, result.message, GsonUtils.gson().toJson(result), hostFlag)
}

internal fun postAction(hostFlag: String): CoroutinesResultPostProcessor {
    return NetContext.get().hostConfig(hostFlag).coroutinesResultPostProcessor() ?: EMPTY_ENTRY
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

    val errorBodyHandler = NetContext.get().hostConfig(hostFlag).errorBodyHandler()

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