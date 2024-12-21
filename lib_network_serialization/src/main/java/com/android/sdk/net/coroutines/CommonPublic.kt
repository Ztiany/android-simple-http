package com.android.sdk.net.coroutines

import com.android.sdk.net.NetContext
import com.android.sdk.net.ServiceContext
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.nonnull.internalApiCall
import com.android.sdk.net.coroutines.nonnull.internalApiCallRetry
import com.android.sdk.net.coroutines.nonnull.internalExecuteApiCall
import com.android.sdk.net.coroutines.nonnull.internalExecuteApiCallRetry
import com.android.sdk.net.coroutines.nullable.internalApiCallNullable
import com.android.sdk.net.coroutines.nullable.internalApiCallRetryNullable
import com.android.sdk.net.coroutines.nullable.internalExecuteApiCallNullable
import kotlinx.coroutines.CancellationException

typealias RetryDeterminer = suspend (Int, Throwable) -> Boolean

/**
 * Call an API and get the result wrapped in [CallResult].
 *
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
suspend fun <T : Any> apiCall(
    call: suspend () -> Result<T>,
): CallResult<T> {
    return internalApiCall(NetContext.DEFAULT_CONFIG, call)
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 *
 * @see apiCall
 */
suspend fun <T : Any> apiCall(
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T>,
): CallResult<T> {
    return internalApiCallRetry(NetContext.DEFAULT_CONFIG, retryDeterminer, call)
}

/**
 *
 * Execute an API and get the result directly. You need to take care of the exception handling. your code may be like this:
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
 *
 * Notice:
 *
 * - Catch [CancellationException] will cause coroutines unable to be cancelled.
 *  - The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
suspend fun <T : Any> executeApiCall(
    call: suspend () -> Result<T>,
): T {
    return internalExecuteApiCall(NetContext.DEFAULT_CONFIG, call)
}

/**
 * Notice:
 *
 * - Catch [CancellationException] will cause coroutines unable to be cancelled.
 * - The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 *
 * @see executeApiCall
 */
suspend fun <T : Any> executeApiCall(
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T>,
): T {
    return internalExecuteApiCallRetry(NetContext.DEFAULT_CONFIG, retryDeterminer, call)
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 *
 * @see apiCall
 */
suspend fun <T : Any?> apiCallNullable(
    /**
     * Note: At present(retrofit:2.9.0)，Defining return type of suspend api method as T? is not supported.
     * Responses of Http like 204 will cause an Exception: `kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException`.
     */
    call: suspend () -> Result<T>?,
): CallResult<T?> {
    return internalApiCallNullable(NetContext.DEFAULT_CONFIG, call)
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 *
 * @see apiCall
 */
suspend fun <T : Any?> apiCallNullable(
    retryDeterminer: RetryDeterminer,
    /**
     * Note: At present(retrofit:2.9.0)，Defining return type of suspend api method as T? is not supported.
     * Responses of Http like 204 will cause an Exception: `kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException`.
     */
    call: suspend () -> Result<T>?,
): CallResult<T?> {
    return internalApiCallRetryNullable(NetContext.DEFAULT_CONFIG, retryDeterminer, call)
}

/**
 * Notice:
 *
 * - Catch [CancellationException] will cause coroutines unable to be cancelled.
 * - The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 *
 * @see executeApiCall
 */
suspend fun <T : Any?> executeApiCallNullable(
    /**
     * Note: At present(retrofit:2.9.0)，Defining return type of suspend api method as T? is not supported.
     * Responses of Http like 204 will cause an Exception: `kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException`.
     */
    call: suspend () -> Result<T>?,
): T? {
    return internalExecuteApiCallNullable(NetContext.DEFAULT_CONFIG, call)
}

/**
 * Notice:
 *
 * - Catch [CancellationException] will cause coroutines unable to be cancelled.
 * - The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 *
 * @see executeApiCall
 */
suspend fun <T : Any?> executeApiCallNullable(
    retryDeterminer: RetryDeterminer,
    /**
     * Note: At present(retrofit:2.9.0)，Defining return type of suspend api method as T? is not supported.
     * Responses of Http like 204 will cause an Exception: `kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException`.
     */
    call: suspend () -> Result<T>?,
): T? {
    return internalExecuteApiCallNullable(NetContext.DEFAULT_CONFIG, retryDeterminer, call)
}