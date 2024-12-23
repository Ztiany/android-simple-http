package com.android.sdk.net.core.config

import com.android.sdk.net.NetContext
import retrofit2.HttpException

/**
 * Some servers may return a response body when the HTTP response code is `[400-500)`. Retrofit does not parse the response body in this case.
 * You can provide this interface to [NetContext] to parse the error body when the HTTP request fails.
 */
interface HttpExceptionHandler {

    /**
     * You may return a new exception or return `null` to use the original exception. the new created exception may
     * carry the error message from the response body.
     */
    fun handleException(httpException: HttpException, hostFlag: String): Throwable?

}