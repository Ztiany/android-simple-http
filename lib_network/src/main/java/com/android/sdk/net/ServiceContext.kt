package com.android.sdk.net

import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.RetryDeterminer
import com.android.sdk.net.optional.Optional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface ServiceContext<Service> {

    val service: Service

    ///////////////////////////////////////////////////////////////////////////
    // Coroutines
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Call an API and get the result wrapped in [CallResult].
     */
    suspend fun <T : Any> apiCall(
        call: suspend Service.() -> Result<T>
    ): CallResult<T>

    /**
     * Execute an API and get the result directly.
     *
     * Note: you need to take care of the exception handling. your code may be like this:
     *
     * ```
     *fun smsLogin(phone: String, password: String) {
     *     _loginState.setLoading()
     *     viewModelScope.launch {
     *         try {
     *             val user = accountRepository.pwdLogin(phone, password)
     *             _loginState.setData(user)
     *         } catch (e: Exception) {
     *             // make sure the coroutine is still active. it's necessary to call this method at the majority of the time.
     *             ensureActive()
     *             _loginState.setError(e)
     *         }
     *     }
     * }
     * ```
     */
    suspend fun <T : Any> executeApiCall(
        call: suspend Service.() -> Result<T>
    ): T

    suspend fun <T : Any?> apiCallNullable(
        /**
         * Note: At present(retrofit:2.9.0)，Defining return type of suspend api method as T? is not supported.
         * Responses of Http like 204 will cause an Exception: `kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException`.
         */
        call: suspend Service.() -> Result<T>?
    ): CallResult<T?>

    suspend fun <T : Any?> executeApiCallNullable(
        /**
         * Note: At present(retrofit:2.9.0)，Defining return type of suspend api method as T? is not supported.
         * Responses of Http like 204 will cause an Exception: `kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException`.
         */
        call: suspend Service.() -> Result<T>?
    ): T?

    suspend fun <T : Any> apiCall(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>
    ): CallResult<T>

    suspend fun <T : Any> executeApiCall(
        retryDeterminer: RetryDeterminer,
        call: suspend Service.() -> Result<T>
    ): T

    suspend fun <T : Any?> apiCallNullable(
        retryDeterminer: RetryDeterminer,
        /**
         * Note: At present(retrofit:2.9.0)，Defining return type of suspend api method as T? is not supported.
         * Responses of Http like 204 will cause an Exception: `kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException`.
         */
        call: suspend Service.() -> Result<T>?
    ): CallResult<T?>

    suspend fun <T : Any?> executeApiCallNullable(
        retryDeterminer: RetryDeterminer,
        /**
         * Note: At present(retrofit:2.9.0)，Defining return type of suspend api method as T? is not supported.
         * Responses of Http like 204 will cause an Exception: `kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException`.
         */
        call: suspend Service.() -> Result<T>?
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