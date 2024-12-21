package com.android.sdk.net.impl

import android.text.TextUtils
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.exception.ErrorMessageFactory
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.rxjava2.RxJavaChecker
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * @author Ztiany
 */
internal class ErrorMessageFactoryImpl : ErrorMessageFactory {

    override fun createMessage(exception: Throwable): CharSequence {
        val errorMessageConverter = NetContext.get().commonConfig().errorMessageConverter()

        Timber.d("createMessage with：%s", exception.toString())

        var message: CharSequence? = null
        if (exception is IOException) {
            message = errorMessageConverter.netErrorMessage(exception)
        } else if (exception is ServerErrorException) {
            val errorType = exception.errorType
            if (errorType == ServerErrorException.DATA_PARSE_ERROR) {
                message = errorMessageConverter.serverDataParseErrorMessage(exception)
            } else if (errorType == ServerErrorException.EMPTY_SERVER_DATA) {
                message = errorMessageConverter.nullEntityErrorMessage(exception)
            }
        } else if (exception is HttpException) {
            val code = exception.code()
            if (code >= 500 /*http 500*/) {
                message = errorMessageConverter.serverInternalErrorMessage(exception)
            } else if (code >= 400 /*http 400*/) {
                message = errorMessageConverter.clientRequestErrorMessage(exception)
            }
        } else if (exception is ApiErrorException) {
            message = exception.message
            if (TextUtils.isEmpty(message)) {
                message = errorMessageConverter.apiErrorMessage(exception)
            }
        } else if (RxJavaChecker.hasRxJava2() && exception is NoSuchElementException) {
            message = errorMessageConverter.serverInternalErrorMessage(exception)
        }

        return message.takeIf {
            !it.isNullOrEmpty()
        } ?: errorMessageConverter.unknownErrorMessage(exception)
    }

}