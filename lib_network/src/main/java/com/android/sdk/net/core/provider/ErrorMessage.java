package com.android.sdk.net.core.provider;

import com.android.sdk.net.core.exception.ApiErrorException;

/**
 * @author Ztiany
 */
public interface ErrorMessage {

    /**
     * Generate a message for network error.
     */
    CharSequence netErrorMessage(Throwable exception);

    /**
     * Generate a message for server data format error.
     */
    CharSequence serverDataErrorMessage(Throwable exception);

    /**
     * Generate a message for server returning null entity.
     */
    CharSequence serverReturningNullEntityErrorMessage(Throwable exception);

    /**
     * Generate a message for server internal error(response code = 500-600).
     */
    CharSequence serverInternalErrorMessage(Throwable exception);

    /**
     * Generate a message for client request error(response code = 400-499).
     */
    CharSequence clientRequestErrorMessage(Throwable exception);

    /**
     * Generate a message for api error.
     */
    CharSequence apiErrorMessage(ApiErrorException exception);

    /**
     * Generate a message for unknown error.
     */
    CharSequence unknownErrorMessage(Throwable exception);

}
