package com.android.sdk.net

import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.RetryDeterminer
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface ServiceContext<Service> {

    val service: Service

    ///////////////////////////////////////////////////////////////////////////
    // Coroutines
    ///////////////////////////////////////////////////////////////////////////

    suspend fun <T : Any> apiCall(
        call: suspend Service.() -> Result<T>
    ): CallResult<T>

    suspend fun <T : Any> executeApiCall(
        call: suspend Service.() -> Result<T>
    ): T

    suspend fun <T> apiCallNullable(
        /**目前，retrofit 接口中的 suspend 方法不支持返回 T?，返回注诸如 204 之类响应将会导致 kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException 异常。*/
        call: suspend Service.() -> Result<T?>?
    ): CallResult<T?>

    suspend fun <T> executeApiCallNullable(
        /**目前，retrofit 接口中的 suspend 方法不支持返回 T?，返回注诸如 204 之类响应将会导致 kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException 异常。*/
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
        /**目前，retrofit 接口中的 suspend 方法不支持返回 T?，返回注诸如 204 之类响应将会导致 kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException 异常。*/
        call: suspend Service.() -> Result<T?>?
    ): CallResult<T?>

    suspend fun <T> executeApiCallNullable(
        retryDeterminer: RetryDeterminer,
        /**目前，retrofit 接口中的 suspend 方法不支持返回 T?，返回注诸如 204 之类响应将会导致 kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException 异常。*/
        call: suspend Service.() -> Result<T?>?
    ): T?

    ///////////////////////////////////////////////////////////////////////////
    // Rx2
    ///////////////////////////////////////////////////////////////////////////

    fun <T : Result<E>, E> Observable<T>.optionalExtractor(): Observable<Optional<E>>

    fun <T : Result<E>, E> Observable<T>.resultExtractor(): Observable<E>

    fun <E, T : Result<E>> Observable<T>.resultChecker(): Observable<Result<E>>

    fun <T : Result<E>, E> Flowable<T>.optionalExtractor(): Flowable<Optional<E>>

    fun <T : Result<E>, E> Flowable<T>.resultExtractor(): Flowable<E>

    fun <E, T : Result<E>> Flowable<T>.resultChecker(): Flowable<Result<E>>

    fun <T : Result<E>, E> Single<T>.optionalExtractor(): Single<Optional<E>>

    fun <T : Result<E>, E> Single<T>.resultExtractor(): Single<E>

    fun <E, T : Result<E>> Single<T>.resultChecker(): Single<Result<E>>

}