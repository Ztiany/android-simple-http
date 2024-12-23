package com.android.sdk.net.core.exception

import com.android.sdk.net.core.result.Result

/**
 * @author Ztiany
 */
class ApiErrorException(
    val code: Int,
    message: String?,
    val raw: Result<*>?,
    val hostFlag: String,
) : Exception(message) {

    /**
     * @param code     the error code
     * @param message  the error message
     * @param hostFlag the host flag
     */
    constructor(code: Int, message: String?, hostFlag: String) : this(code, message, null, hostFlag)

}