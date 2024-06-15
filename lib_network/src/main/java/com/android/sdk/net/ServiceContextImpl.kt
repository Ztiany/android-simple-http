package com.android.sdk.net

import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.RetryDeterminer
import com.android.sdk.net.rxjava2.internalOptionalExtractor
import com.android.sdk.net.rxjava2.internalResultChecker
import com.android.sdk.net.rxjava2.internalResultExtractor
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.CancellationException

internal class ServiceContextImpl<Service>(
    val hostFlag: String,
    override val service: Service,
) : ServiceContext<Service> {

    ///////////////////////////////////////////////////////////////////////////
    // Coroutines
    ///////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////
    // Rx2
    ///////////////////////////////////////////////////////////////////////////

    override fun <T : Result<E>, E> Observable<T>.optionalExtractor(): Observable<Optional<E>> {
        return this.internalOptionalExtractor(hostFlag)
    }

    override fun <T : Result<E>, E> Observable<T>.resultExtractor(): Observable<E> {
        return this.internalResultExtractor(hostFlag)
    }

    override fun <E, T : Result<E>> Observable<T>.resultChecker(): Observable<Result<E>> {
        return this.internalResultChecker(hostFlag)
    }

    override fun <T : Result<E>, E> Flowable<T>.optionalExtractor(): Flowable<Optional<E>> {
        return this.internalOptionalExtractor(hostFlag)
    }

    override fun <T : Result<E>, E> Flowable<T>.resultExtractor(): Flowable<E> {
        return this.internalResultExtractor(hostFlag)
    }

    override fun <E, T : Result<E>> Flowable<T>.resultChecker(): Flowable<Result<E>> {
        return this.internalResultChecker(hostFlag)
    }

    override fun <T : Result<E>, E> Single<T>.optionalExtractor(): Single<Optional<E>> {
        return this.internalOptionalExtractor(hostFlag)
    }

    override fun <T : Result<E>, E> Single<T>.resultExtractor(): Single<E> {
        return this.internalResultExtractor(hostFlag)
    }

    override fun <E, T : Result<E>> Single<T>.resultChecker(): Single<Result<E>> {
        return this.internalResultChecker(hostFlag)
    }

}