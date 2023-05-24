package com.android.sdk.net

import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.RetryDeterminer

interface SpecializedService<Service> {

    val service: Service

    suspend fun <T : Any> apiCall(
        call: suspend Service.() -> Result<T>
    ): CallResult<T>

    suspend fun <T : Any> executeApiCall(
        call: suspend Service.() -> Result<T>
    ): T

    suspend fun <T> apiCallNullable(
        call: suspend Service.() -> Result<T?>?
    ): CallResult<T?>

    suspend fun <T> executeApiCallNullable(
        call: suspend Service.() -> Result<T?>?
    ): T?

    suspend fun <T : Any> apiCallRetry(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>
    ): CallResult<T>

    suspend fun <T : Any> executeApiCallRetry(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>
    ): T

    suspend fun <T> apiCallRetryNullable(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T?>?
    ): CallResult<T?>

    suspend fun <T> executeApiCallNullable(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T?>?
    ): T?

}