package com.android.sdk.net.core.config;

import androidx.annotation.NonNull;

import com.android.sdk.net.core.exception.ApiErrorException;
import com.android.sdk.net.core.exception.ServerErrorException;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ztiany
 */
public interface ErrorListener {

    void onApiException(@NonNull ApiErrorException apiErrorException, @NotNull String hostFlag);

    void onParsingDataFailed(@NonNull ServerErrorException exception, @NotNull String hostFlag);

    void onServerDataNotReturned(@NonNull ServerErrorException exception, @NotNull String hostFlag);

}