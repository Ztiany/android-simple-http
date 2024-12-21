package com.android.sdk.net.impl

import com.android.sdk.net.ServiceContext
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.RetryDeterminer
import kotlinx.coroutines.CancellationException

internal class ServiceContextImpl<Service>(
    override val hostFlag: String,
    override val service: Service,
) : ServiceContext<Service> {

    override suspend fun <T : Any> apiCall(call: suspend Service.() -> Result<T>): CallResult<T> {
        return com.android.sdk.net.coroutines.nonnull.internalApiCall(hostFlag) {
            call(service)
        }
    }

    /** Notice: Catch [CancellationException] will cause coroutines unable to be cancelled. */
    override suspend fun <T : Any> executeApiCall(call: suspend Service.() -> Result<T>): T {
        return com.android.sdk.net.coroutines.nonnull.internalExecuteApiCall(hostFlag) {
            call(service)
        }
    }

    override suspend fun <T : Any?> apiCallNullable(call: suspend Service.() -> Result<T>?): CallResult<T?> {
        return com.android.sdk.net.coroutines.nullable.internalApiCallNullable(hostFlag) {
            call(service)
        }
    }

    /** Notice: Catch [CancellationException] will cause coroutines unable to be cancelled. */
    override suspend fun <T : Any?> executeApiCallNullable(call: suspend Service.() -> Result<T>?): T? {
        return com.android.sdk.net.coroutines.nullable.internalExecuteApiCallNullable(hostFlag) {
            call(service)
        }
    }

    override suspend fun <T : Any> apiCall(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>,
    ): CallResult<T> {
        return com.android.sdk.net.coroutines.nonnull.internalApiCallRetry(hostFlag, retryDeterminer) {
            call(service)
        }
    }

    /** Notice: Catch [CancellationException] will cause coroutines unable to be cancelled. */
    override suspend fun <T : Any> executeApiCall(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>,
    ): T {
        return com.android.sdk.net.coroutines.nonnull.internalExecuteApiCallRetry(hostFlag, retryDeterminer) {
            call(service)
        }
    }

    override suspend fun <T : Any?> apiCallNullable(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>?,
    ): CallResult<T?> {
        return com.android.sdk.net.coroutines.nullable.internalApiCallRetryNullable(hostFlag, retryDeterminer) {
            call(service)
        }
    }

    /** Notice: Catch [CancellationException] will cause coroutines unable to be cancelled. */
    override suspend fun <T : Any?> executeApiCallNullable(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>?,
    ): T? {
        return com.android.sdk.net.coroutines.nullable.internalExecuteApiCallNullable(hostFlag, retryDeterminer) {
            call(service)
        }
    }

}