package com.android.sdk.net.core.exception;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Ztiany
 */
public class ApiErrorException extends Exception {

    private final int mCode;

    @NonNull private final String mHostFlag;

    @Nullable private final String mRow;

    /**
     * @param code     the error code
     * @param message  the error message
     * @param hostFlag the host flag
     */
    public ApiErrorException(int code, @Nullable String message, @NonNull String hostFlag) {
        this(code, message, null, hostFlag);
    }

    /**
     * @param code     the error code
     * @param message  the error message
     * @param row      the error row
     * @param hostFlag the host flag
     */
    public ApiErrorException(int code, @Nullable String message, @Nullable String row, @NonNull String hostFlag) {
        super(message);
        mCode = code;
        mHostFlag = hostFlag;
        mRow = row;
    }

    public int getCode() {
        return mCode;
    }

    @Nullable
    public String getRow() {
        return mRow;
    }

    @NonNull
    public String getHostFlag() {
        return mHostFlag;
    }

}