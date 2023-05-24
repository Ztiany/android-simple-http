package com.android.sdk.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.android.sdk.net.core.exception.ApiErrorException;
import com.android.sdk.net.core.exception.ErrorMessageFactory;
import com.android.sdk.net.core.exception.ServerErrorException;
import com.android.sdk.net.core.provider.ErrorMessage;
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
        ErrorMessage errorMessage = NetContext.get().commonProvider().errorMessage();

        Timber.d("createMessage with：%s", exception.toString());

        CharSequence message = null;
        //SocketTimeoutException
        //1：网络连接错误处理
        if (exception instanceof IOException) {
            message = errorMessage.netErrorMessage(exception);
        }

        //2：服务器错误处理
        else if (exception instanceof ServerErrorException) {
            int errorType = ((ServerErrorException) exception).getErrorType();
            if (errorType == ServerErrorException.SERVER_DATA_ERROR) {
                message = errorMessage.serverDataErrorMessage(exception);
            } else if (errorType == ServerErrorException.SERVER_NULL_DATA) {
                message = errorMessage.serverReturningNullEntityErrorMessage(exception);
            }
        }

        //3：响应码非 200 处理
        else if (exception instanceof HttpException) {
            int code = ((HttpException) exception).code();
            if (code >= 500/*http 500 表示服务器错误*/) {
                message = errorMessage.serverInternalErrorMessage(exception);
            } else if (code >= 400/*http 400 表示客户端请求出错*/) {
                message = errorMessage.clientRequestErrorMessage(exception);
            }
        }

        //4：Api Error
        else if (exception instanceof ApiErrorException) {
            message = exception.getMessage();
            if (TextUtils.isEmpty(message)) {
                message = errorMessage.apiErrorMessage((ApiErrorException) exception);
            }
        }

        //6：RxJava Single
        else if (RxJavaChecker.hasRxJava2() && exception instanceof NoSuchElementException) {
            message = errorMessage.serverInternalErrorMessage(exception);
        }

        //7：Others
        if (isEmpty(message)) {
            message = errorMessage.unknownErrorMessage(exception);
        }

        return message;
    }

    private static boolean isEmpty(CharSequence str) {
        return str == null || str.toString().trim().length() == 0;
    }

}