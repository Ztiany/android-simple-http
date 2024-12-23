package com.android.sdk.net.impl

import android.text.TextUtils
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.config.ErrorMessageFactory
import com.android.sdk.net.core.exception.ServerErrorException
import retrofit2.HttpException
import java.io.IOException

/**
 * @author Ztiany
 */
internal class ErrorMessageFactoryImpl : ErrorMessageFactory {

    override fun convert(exception: Throwable): CharSequence {
        val errorMessageConverter = NetContext.get().commonConfig().errorMessageConverter()

        var message: CharSequence? = null
        if (exception is IOException) {
            message = errorMessageConverter.convertWhenNetError(exception)
        } else if (exception is ServerErrorException) {
            val errorType = exception.errorType
            if (errorType == ServerErrorException.DATA_PARSE_ERROR) {
                message = errorMessageConverter.convertWhenParsingDataFailed(exception)
            } else if (errorType == ServerErrorException.EMPTY_SERVER_DATA) {
                message = errorMessageConverter.convertWhenNoDataReturned(exception)
            }
        } else if (exception is HttpException) {
            val code = exception.code()
            if (code >= 500 /*http 500*/) {
                message = errorMessageConverter.convertWhenServerInternalError(exception)
            } else if (code >= 400 /*http 400*/) {
                message = errorMessageConverter.convertWhenClientRequestFailed(exception)
            }
        } else if (exception is ApiErrorException) {
            message = exception.message
            if (TextUtils.isEmpty(message)) {
                message = errorMessageConverter.convertWhenApiException(exception)
            }
        }

        return message.takeIf {
            !it.isNullOrEmpty()
        } ?: errorMessageConverter.convertWhenUnknownError(exception)
    }

}