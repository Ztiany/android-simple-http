package com.android.sdk.net

import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.RetryDeterminer
import com.android.sdk.net.rxjava2.ResultHandlers
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

internal class ServiceContextImpl<Service>(
    val hostFlag: String,
    override val service: Service
) : ServiceContext<Service> {

    ///////////////////////////////////////////////////////////////////////////
    // Coroutines
    ///////////////////////////////////////////////////////////////////////////

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

    override suspend fun <T : Any?> apiCallNullable(call: suspend Service.() -> Result<T>?): CallResult<T?> {
        return com.android.sdk.net.coroutines.nullable.apiCallNullable(hostFlag) {
            call(service)
        }
    }

    override suspend fun <T : Any?> executeApiCallNullable(call: suspend Service.() -> Result<T>?): T? {
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

    override suspend fun <T : Any?> apiCallRetryNullable(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>?
    ): CallResult<T?> {
        return com.android.sdk.net.coroutines.nullable.apiCallRetryNullable(hostFlag, retryDeterminer) {
            call(service)
        }
    }

    override suspend fun <T : Any?> executeApiCallNullable(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>?
    ): T? {
        return com.android.sdk.net.coroutines.nullable.executeApiCallNullable(hostFlag, retryDeterminer) {
            call(service)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Rx2
    ///////////////////////////////////////////////////////////////////////////

    override fun <T : Result<E>, E> Observable<T>.optionalExtractor(): Observable<Optional<E>> {
        return this.compose(ResultHandlers.newOptionalExtractor(hostFlag))
    }

    override fun <T : Result<E>, E> Observable<T>.resultExtractor(): Observable<E> {
        return this.compose(ResultHandlers.newExtractor(hostFlag))
    }

    override fun <E, T : Result<E>> Observable<T>.resultChecker(): Observable<Result<E>> {
        return (this.compose(ResultHandlers.newResultChecker(hostFlag)))
    }

    override fun <T : Result<E>, E> Flowable<T>.optionalExtractor(): Flowable<Optional<E>> {
        return this.compose(ResultHandlers.newOptionalExtractor(hostFlag))
    }

    override fun <T : Result<E>, E> Flowable<T>.resultExtractor(): Flowable<E> {
        return this.compose(ResultHandlers.newExtractor(hostFlag))
    }

    override fun <E, T : Result<E>> Flowable<T>.resultChecker(): Flowable<Result<E>> {
        return (this.compose(ResultHandlers.newResultChecker(hostFlag)))
    }

    override fun <T : Result<E>, E> Single<T>.optionalExtractor(): Single<Optional<E>> {
        return this.compose(ResultHandlers.newOptionalExtractor(hostFlag))
    }

    override fun <T : Result<E>, E> Single<T>.resultExtractor(): Single<E> {
        return this.compose(ResultHandlers.newExtractor(hostFlag))
    }

    override fun <E, T : Result<E>> Single<T>.resultChecker(): Single<Result<E>> {
        return (this.compose(ResultHandlers.newResultChecker(hostFlag)))
    }

}