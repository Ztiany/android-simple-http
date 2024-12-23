package com.android.sdk.net.core.exception

/**
 * @author Ztiany
 */
class ServerErrorException : RuntimeException {

    val errorType: Int

    /**
     * @param errorType [.DATA_PARSE_ERROR] or [.EMPTY_SERVER_DATA]
     */
    constructor(errorType: Int, cause: Exception) : super(cause) {
        this.errorType = errorType
    }

    /**
     * @param errorType [.DATA_PARSE_ERROR] or [.EMPTY_SERVER_DATA]
     */
    constructor(errorType: Int) {
        this.errorType = errorType
    }

    override fun toString(): String {
        val string = super.toString()
        return string + "ErrorType = " + name()
    }

    private fun name(): String {
        return when (errorType) {
            DATA_PARSE_ERROR -> "DATA_PARSE_ERROR"
            EMPTY_SERVER_DATA -> "EMPTY_SERVER_DATA"
            else -> "Unknown"
        }
    }

    companion object {
        const val DATA_PARSE_ERROR: Int = 1
        const val EMPTY_SERVER_DATA: Int = 2
    }

}