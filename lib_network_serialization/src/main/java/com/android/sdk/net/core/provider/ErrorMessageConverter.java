package com.android.sdk.net.core.provider;

import androidx.annotation.NonNull;

import com.android.sdk.net.core.exception.ApiErrorException;

/**
 * Convert error to message.
 *
 * @author Ztiany
 */
public interface ErrorMessageConverter {

    /**
     * Generate a message for network error.
     */
    CharSequence netErrorMessage(@NonNull Throwable throwable);

    /**
     * Generate a message for server data format error.
     */
    CharSequence serverDataParseErrorMessage(@NonNull Throwable throwable);

    /**
     * Generate a message for server returning null entity.
     */
    CharSequence nullEntityErrorMessage(@NonNull Throwable throwable);

    /**
     * Generate a message for server internal error(response code = 500-600).
     */
    CharSequence serverInternalErrorMessage(@NonNull Throwable throwable);

    /**
     * Generate a message for client request error(response code = 400-499).
     */
    CharSequence clientRequestErrorMessage(@NonNull Throwable throwable);

    /**
     * Generate a message for api error.
     */
    CharSequence apiErrorMessage(@NonNull ApiErrorException exception);

    /**
     * Generate a message for unknown error.
     */
    CharSequence unknownErrorMessage(@NonNull Throwable exception);

}
