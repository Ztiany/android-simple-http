package com.android.sdk.net

import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.RetryDeterminer
import com.android.sdk.net.rxjava2.ResultHandlers
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

internal class SpecializedServiceImpl<Service>(
    val hostFlag: String,
    override val service: Service
) : SpecializedService<Service> {

    override suspend fun <T : Any> apiCall(call: suspend Service.() -> Result<T>): CallResult<T> {
        return com.android.sdk.net.coroutines.nonnull.apiCall(hostFlag) {
            call(service)
        }
    }

    override suspend fun <T : Any> executeApiCall(call: suspend Service.() -> Result<T>): T {
        return com.android.sdk.net.coroutines.nonnull.executeApiCall(hostFlag) {
            call(service)
        }
    }

    override suspend fun <T> apiCallNullable(call: suspend Service.() -> Result<T?>?): CallResult<T?> {
        return com.android.sdk.net.coroutines.nullable.apiCallNullable(hostFlag) {
            call(service)
        }
    }

    override suspend fun <T> executeApiCallNullable(call: suspend Service.() -> Result<T?>?): T? {
        return com.android.sdk.net.coroutines.nullable.executeApiCallNullable(hostFlag) {
            call(service)
        }
    }

    override suspend fun <T : Any> apiCallRetry(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>
    ): CallResult<T> {
        return com.android.sdk.net.coroutines.nonnull.apiCallRetry(hostFlag, retryDeterminer) {
            call(service)
        }
    }

    override suspend fun <T : Any> executeApiCallRetry(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>
    ): T {
        return com.android.sdk.net.coroutines.nonnull.executeApiCallRetry(hostFlag, retryDeterminer) {
            call(service)
        }
    }

    override suspend fun <T> apiCallRetryNullable(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T?>?
    ): CallResult<T?> {
        return com.android.sdk.net.coroutines.nullable.apiCallRetryNullable(hostFlag, retryDeterminer) {
            call(service)
        }
    }

    override suspend fun <T> executeApiCallNullable(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T?>?
    ): T? {
        return com.android.sdk.net.coroutines.nullable.executeApiCallNullable(hostFlag, retryDeterminer) {
            call(service)
        }
    }

}