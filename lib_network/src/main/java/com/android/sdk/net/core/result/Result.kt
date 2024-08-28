package com.android.sdk.net.core.result

/**
 * A interface to represent the result of a network request.
 */
interface Result<T> {

    val data: T

    val code: Int

    val message: String

    val isSuccess: Boolean

}