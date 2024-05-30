package com.android.sdk.net.rxjava2

import com.android.sdk.net.NetContext
import com.android.sdk.net.ServiceContext
import com.android.sdk.net.core.result.Result
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

///////////////////////////////////////////////////////////////////////////
// Public
///////////////////////////////////////////////////////////////////////////
/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
fun <T : Result<E>, E> Observable<T>.optionalExtractor(): Observable<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor())
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
fun <T : Result<E>, E> Observable<T>.resultExtractor(): Observable<E> {
    return this.compose(ResultHandlers.resultExtractor())
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
fun <E, T : Result<E>> Observable<T>.resultChecker(): Observable<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker()))
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
fun <T : Result<E>, E> Flowable<T>.optionalExtractor(): Flowable<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor())
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
fun <T : Result<E>, E> Flowable<T>.resultExtractor(): Flowable<E> {
    return this.compose(ResultHandlers.resultExtractor())
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
fun <E, T : Result<E>> Flowable<T>.resultChecker(): Flowable<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker()))
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
fun <T : Result<E>, E> Single<T>.optionalExtractor(): Single<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor())
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
fun <T : Result<E>, E> Single<T>.resultExtractor(): Single<E> {
    return this.compose(ResultHandlers.resultExtractor())
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
fun <E, T : Result<E>> Single<T>.resultChecker(): Single<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker()))
}

///////////////////////////////////////////////////////////////////////////
// Internal
///////////////////////////////////////////////////////////////////////////

internal fun <T : Result<E>, E> Observable<T>.internalOptionalExtractor(hostFlag: String): Observable<Optional<E>> {
    return this.compose(ResultHandlers.newOptionalExtractor(hostFlag))
}

internal fun <T : Result<E>, E> Observable<T>.internalResultExtractor(hostFlag: String): Observable<E> {
    return this.compose(ResultHandlers.newExtractor(hostFlag))
}

internal fun <E, T : Result<E>> Observable<T>.internalResultChecker(hostFlag: String): Observable<Result<E>> {
    return (this.compose(ResultHandlers.newResultChecker(hostFlag)))
}

internal fun <T : Result<E>, E> Flowable<T>.internalOptionalExtractor(hostFlag: String): Flowable<Optional<E>> {
    return this.compose(ResultHandlers.newOptionalExtractor(hostFlag))
}

internal fun <T : Result<E>, E> Flowable<T>.internalResultExtractor(hostFlag: String): Flowable<E> {
    return this.compose(ResultHandlers.newExtractor(hostFlag))
}

internal fun <E, T : Result<E>> Flowable<T>.internalResultChecker(hostFlag: String): Flowable<Result<E>> {
    return (this.compose(ResultHandlers.newResultChecker(hostFlag)))
}

internal fun <T : Result<E>, E> Single<T>.internalOptionalExtractor(hostFlag: String): Single<Optional<E>> {
    return this.compose(ResultHandlers.newOptionalExtractor(hostFlag))
}

internal fun <T : Result<E>, E> Single<T>.internalResultExtractor(hostFlag: String): Single<E> {
    return this.compose(ResultHandlers.newExtractor(hostFlag))
}

internal fun <E, T : Result<E>> Single<T>.internalResultChecker(hostFlag: String): Single<Result<E>> {
    return (this.compose(ResultHandlers.newResultChecker(hostFlag)))
}