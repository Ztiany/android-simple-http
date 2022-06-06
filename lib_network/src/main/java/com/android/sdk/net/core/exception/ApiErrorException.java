package com.android.sdk.net.core.exception;

/**
 * ApiErrorException 表示当调用接口失败。
 *
 * @author Ztiany
 */
public class ApiErrorException extends Exception {

    private final int mCode;
    private final String mFlag;

    public ApiErrorException(int code, String message, String flag) {
        super(message);
        mCode = code;
        mFlag = flag;
    }

    public int getCode() {
        return mCode;
    }

    public String getFlag() {
        return mFlag;
    }

}
