package com.android.sdk.net.coroutines

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.core.result.Result

internal suspend fun <T> apiCallInternal(
    hostFlag: String,
    requireNonNullData: Boolean,
    call: suspend () -> Result<T>?,
): CallResult<T> {

    val postAction = postAction(hostFlag)

    var result = realCall(call, requireNonNullData, hostFlag)

    if (result is CallResult.Error && postAction.retry(result.error)) {
        result = realCall(call, requireNonNullData, hostFlag)
    }

    return result
}

private suspend fun <T> realCall(
    call: suspend () -> Result<T>?,
    requireNonNullData: Boolean,
    hostFlag: String,
): CallResult<T> {

    val netContext = NetContext.get()
    val hostConfig = netContext.hostConfig(hostFlag)

    val result: Result<T>?

    try {
        result = call.invoke()
    } catch (throwable: Throwable) {
        return CallResult.Error(transformHttpException(hostFlag, throwable))
    }

    // TODO: 支持自定义处理
    if (result == null) {
        val serverErrorException = ServerErrorException(ServerErrorException.EMPTY_SERVER_DATA)
        hostConfig.errorListener()?.onDataNotReturned(serverErrorException, hostFlag)
        throw serverErrorException
    }

    return if (!result.isSuccess) { //检测响应码是否正确

        CallResult.Error(createApiException(result, hostFlag).apply {
            hostConfig.errorListener()?.onApiException(this, hostFlag)
        })

    } else if (requireNonNullData) { //如果约定必须返回的数据却没有返回数据，则认为是服务器错误。

        val data: T? = result.data
        if (data == null) {
            val error = ServerErrorException(ServerErrorException.EMPTY_SERVER_DATA)
            hostConfig.errorListener()?.onDataNotReturned(error, hostFlag)
            CallResult.Error(error)
        } else {
            CallResult.Success(data)
        }

    } else {
        CallResult.Success(result.data)
    }

}

sealed class CallResult<out T> {

    class Success<out T>(val data: T) : CallResult<T>()

    class Error(val error: Throwable) : CallResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success [data: $data]"
            is Error -> "Error [exception: $error]"
        }
    }

}

inline infix fun <T> CallResult<T>.onSuccess(onSuccess: (T) -> Unit): CallResult<T> {
    if (this is CallResult.Success) {
        onSuccess(this.data)
    }
    return this
}

inline infix fun <T> CallResult<T>.onError(onFailed: (Throwable) -> Unit): CallResult<T> {
    if (this is CallResult.Error) {
        onFailed(this.error)
    }
    return this
}