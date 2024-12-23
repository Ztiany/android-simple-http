package com.android.sdk.net.core.config;

import androidx.annotation.NonNull;

import com.android.sdk.net.core.exception.ApiErrorException;
import com.android.sdk.net.core.exception.ServerErrorException;

import java.io.IOException;

import retrofit2.HttpException;

/**
 * Convert error to message.
 *
 * @author Ztiany
 */
public interface ErrorMessageConverter {

    /**
     * Generate a message for network error.
     */
    CharSequence convertWhenNetError(@NonNull IOException ioException);

    /**
     * Generate a message for server data format error.
     */
    CharSequence convertWhenParsingFailed(@NonNull ServerErrorException serverErrorException);

    /**
     * Generate a message for server returning null entity.
     */
    CharSequence convertWhenNoDataReturned(@NonNull ServerErrorException serverErrorException);

    /**
     * Generate a message for server internal error(response code = 500-600).
     */
    CharSequence convertWhenServerInternalError(@NonNull HttpException httpException);

    /**
     * Generate a message for client request error(response code = 400-499).
     */
    CharSequence convertWhenClientRequestFailed(@NonNull HttpException httpException);

    /**
     * Generate a message for api error.
     */
    CharSequence convertWhenApiError(@NonNull ApiErrorException exception);

    /**
     * Generate a message for unknown error.
     */
    CharSequence convertWhenUnknownError(@NonNull Throwable exception);

}
