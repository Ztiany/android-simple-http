package com.android.sdk.net.core.exception;

import androidx.annotation.NonNull;

/**
 * @author Ztiany
 */
public class ServerErrorException extends RuntimeException {

    private final int mErrorType;

    public static final int DATA_PARSE_ERROR = 1;
    public static final int EMPTY_SERVER_DATA = 2;

    /**
     * @param errorType {@link #DATA_PARSE_ERROR} or {@link #EMPTY_SERVER_DATA}
     */
    public ServerErrorException(int errorType, @NonNull Exception cause) {
        super(cause);
        mErrorType = errorType;
    }

    /**
     * @param errorType {@link #DATA_PARSE_ERROR} or {@link #EMPTY_SERVER_DATA}
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
            case DATA_PARSE_ERROR -> "DATA_PARSE_ERROR";
            case EMPTY_SERVER_DATA -> "EMPTY_SERVER_DATA";
            default -> "Unknown";
        };
    }

}