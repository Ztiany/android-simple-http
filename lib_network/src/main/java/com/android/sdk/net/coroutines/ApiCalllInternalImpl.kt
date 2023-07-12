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

    val result: Result<T>?

    try {
        result = call.invoke()
    } catch (throwable: Throwable) {
        return CallResult.Error(transformHttpException(hostFlag, throwable))
    }

    //TODO: 支持自定义处理
    if (result == null) {
        throw ServerErrorException(ServerErrorException.SERVER_NULL_DATA)
    }

    val netContext = NetContext.get()
    val netProvider = netContext.hostConfigProvider(hostFlag)

    return if (!result.isSuccess) { //检测响应码是否正确

        netProvider.aipHandler()?.onApiError(result, hostFlag)
        CallResult.Error(createApiException(result, hostFlag, netProvider))

    } else if (requireNonNullData) { //如果约定必须返回的数据却没有返回数据，则认为是服务器错误。

        val data: T? = result.data
        if (data == null) {
            CallResult.Error(ServerErrorException(ServerErrorException.SERVER_NULL_DATA))
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