package com.android.sdk.net.core.provider

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException

/**
 * Some servers may return a response body when the HTTP response code is `[400-500)`. Retrofit does not parse the response body in this case.
 * You can provide this interface to [NetContext] to parse the error body when the HTTP request fails.
 */
interface ErrorBodyParser {

    fun parseErrorBody(errorBody: String, hostFlag: String): ApiErrorException?

}