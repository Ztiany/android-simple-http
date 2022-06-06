package com.android.sdk.net.core.exception;

import androidx.annotation.NonNull;

/**
 * 表示服务器异常
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
        String result = "";
        switch (mErrorType) {
            case SERVER_DATA_ERROR:
                result = "SERVER_DATA_ERROR";
                break;
            case SERVER_NULL_DATA:
                result = "SERVER_NO_DATA";
                break;
        }
        return result;
    }

}