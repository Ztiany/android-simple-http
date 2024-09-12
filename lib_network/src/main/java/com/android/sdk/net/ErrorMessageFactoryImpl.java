package com.android.sdk.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.android.sdk.net.core.exception.ApiErrorException;
import com.android.sdk.net.core.exception.ErrorMessageFactory;
import com.android.sdk.net.core.exception.ServerErrorException;
import com.android.sdk.net.core.provider.ErrorMessageConverter;
import com.android.sdk.net.rxjava2.RxJavaChecker;

import java.io.IOException;
import java.util.NoSuchElementException;

import retrofit2.HttpException;
import timber.log.Timber;

/**
 * @author Ztiany
 */
final class ErrorMessageFactoryImpl implements ErrorMessageFactory {

    @NonNull
    @Override
    public CharSequence createMessage(@NonNull Throwable exception) {
        ErrorMessageConverter errorMessageConverter = NetContext.get().commonConfig().errorMessageConverter();

        Timber.d("createMessage with：%s", exception.toString());

        CharSequence message = null;
        //1：Network Connection Error Processing
        if (exception instanceof IOException) {
            message = errorMessageConverter.netErrorMessage(exception);
        }

        //2：Server Error Processing
        else if (exception instanceof ServerErrorException) {
            int errorType = ((ServerErrorException) exception).getErrorType();
            if (errorType == ServerErrorException.DATA_PARSE_ERROR) {
                message = errorMessageConverter.serverDataParseErrorMessage(exception);
            } else if (errorType == ServerErrorException.EMPTY_SERVER_DATA) {
                message = errorMessageConverter.nullEntityErrorMessage(exception);
            }
        }

        //3：Http Response Code is not 200
        else if (exception instanceof HttpException) {
            int code = ((HttpException) exception).code();
            if (code >= 500/*http 500*/) {
                message = errorMessageConverter.serverInternalErrorMessage(exception);
            } else if (code >= 400/*http 400*/) {
                message = errorMessageConverter.clientRequestErrorMessage(exception);
            }
        }

        //4：Api Error
        else if (exception instanceof ApiErrorException) {
            message = exception.getMessage();
            if (TextUtils.isEmpty(message)) {
                message = errorMessageConverter.apiErrorMessage((ApiErrorException) exception);
            }
        }

        //5：RxJava Single
        else if (RxJavaChecker.hasRxJava2() && exception instanceof NoSuchElementException) {
            message = errorMessageConverter.serverInternalErrorMessage(exception);
        }

        //6：Others
        if (isEmpty(message)) {
            message = errorMessageConverter.unknownErrorMessage(exception);
        }

        return message;
    }

    private static boolean isEmpty(CharSequence str) {
        return str == null || str.toString().trim().isEmpty();
    }

}