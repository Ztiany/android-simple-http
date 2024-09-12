package com.android.sdk.net.core.provider;

import androidx.annotation.NonNull;

import com.android.sdk.net.core.exception.ServerErrorException;
import com.android.sdk.net.core.result.Result;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;

/**
 * @author Ztiany
 */
public interface ErrorListener {

    void onApiError(@NonNull Result<?> result, @NotNull String hostFlag);

    void onServerDataEmptyError(@NonNull ServerErrorException exception, @NotNull String hostFlag);

    void onServerDataParseError(@NonNull ServerErrorException exception, @NotNull ResponseBody body, @NotNull String hostFlag);

}