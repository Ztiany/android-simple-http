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
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
suspend fun <T : Any> apiCall(
    call: suspend () -> Result<T>,
): CallResult<T> {
    return internalApiCall(NetContext.DEFAULT_CONFIG, call)
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
suspend fun <T : Any> apiCallRetry(
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T>,
): CallResult<T> {
    return internalApiCallRetry(NetContext.DEFAULT_CONFIG, retryDeterminer, call)
}

/**
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
 */
suspend fun <T : Any> executeApiCallRetry(
    retryDeterminer: RetryDeterminer,
    call: suspend () -> Result<T>,
): T {
    return internalExecuteApiCallRetry(NetContext.DEFAULT_CONFIG, retryDeterminer, call)
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
suspend fun <T : Any?> apiCallNullable(
    /**目前，retrofit 接口中的 suspend 方法不支持返回 T?，返回注诸如 204 之类响应将会导致 kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException 异常。*/
    call: suspend () -> Result<T>?,
): CallResult<T?> {
    return internalApiCallNullable(NetContext.DEFAULT_CONFIG, call)
}

/**
 * Notice: The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
suspend fun <T : Any?> apiCallRetryNullable(
    retryDeterminer: RetryDeterminer,
    /**目前，retrofit 接口中的 suspend 方法不支持返回 T?，返回注诸如 204 之类响应将会导致 kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException 异常。*/
    call: suspend () -> Result<T>?,
): CallResult<T?> {
    return internalApiCallRetryNullable(NetContext.DEFAULT_CONFIG, retryDeterminer, call)
}

/**
 * Notice:
 *
 * - Catch [CancellationException] will cause coroutines unable to be cancelled.
 * - The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
suspend fun <T : Any?> executeApiCallNullable(
    /**目前，retrofit 接口中的 suspend 方法不支持返回 T?，返回注诸如 204 之类响应将会导致 kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException 异常。*/
    call: suspend () -> Result<T>?,
): T? {
    return internalExecuteApiCallNullable(NetContext.DEFAULT_CONFIG, call)
}

/**
 * Notice:
 *
 * - Catch [CancellationException] will cause coroutines unable to be cancelled.
 * - The config of this call is corresponded with [NetContext.DEFAULT_CONFIG]. If you have multi configs, please use [ServiceContext].
 */
suspend fun <T : Any?> executeApiCallNullable(
    retryDeterminer: RetryDeterminer,
    /**目前，retrofit 接口中的 suspend 方法不支持返回 T?，返回注诸如 204 之类响应将会导致 kotlin.KotlinNullPointerException: Response from xxx was null but response body type was declared as non-null KotlinNullPointerException 异常。*/
    call: suspend () -> Result<T>?,
): T? {
    return internalExecuteApiCallNullable(NetContext.DEFAULT_CONFIG, retryDeterminer, call)
}