package com.android.sdk.net.core.exception;

import androidx.annotation.NonNull;

/**
 * @author Ztiany
 */
public class ServerErrorException extends RuntimeException {

    private final int mErrorType;

    public static final int SERVER_DATA_ERROR = 1;
    public static final int SERVER_NULL_DATA = 2;

    /**
     * @param errorType {@link #SERVER_DATA_ERROR} or {@link #SERVER_NULL_DATA}
     */
    public ServerErrorException(int errorType) {
        mErrorType = errorType;
    }

    public int getErrorType() {
        return mErrorType;
    }

    @NonNull
    @Override
    public String toString() {
        String string = super.toString();
        return string + "ErrorType = " + transform();
    }

    private String transform() {
        return switch (mErrorType) {
            case SERVER_DATA_ERROR -> "SERVER_DATA_ERROR";
            case SERVER_NULL_DATA -> "SERVER_NO_DATA";
            default -> "";
        };
    }

}