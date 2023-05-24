package com.android.sdk.net.core.exception;

/**
 * @author Ztiany
 */
public class ApiErrorException extends Exception {

    private final int mCode;
    private final String mHostFlag;

    public ApiErrorException(int code, String message, String hostFlag) {
        super(message);
        mCode = code;
        mHostFlag = hostFlag;
    }

    public int getCode() {
        return mCode;
    }

    public String getHostFlag() {
        return mHostFlag;
    }

}
