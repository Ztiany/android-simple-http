package com.android.sdk.net.core.provider;

import com.android.sdk.net.core.result.Result;

import androidx.annotation.NonNull;

/**
 * @author Ztiany
 */
public interface ApiHandler {

    void onApiError(@NonNull Result<?> result, String hostFlag);

}