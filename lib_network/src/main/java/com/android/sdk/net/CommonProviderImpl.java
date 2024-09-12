package com.android.sdk.net;

import androidx.annotation.NonNull;

import com.android.sdk.net.core.provider.ErrorMessageConverter;
import com.android.sdk.net.core.provider.PlatformInteractor;

final class CommonProviderImpl implements CommonProvider {

    ErrorMessageConverter mErrorMessageConverter;

    PlatformInteractor mPlatformInteractor;

    @NonNull
    @Override
    public ErrorMessageConverter errorMessageConverter() {
        return mErrorMessageConverter;
    }

    @NonNull
    @Override
    public PlatformInteractor platformInteractor() {
        return mPlatformInteractor;
    }

    public void checkRequirement() {
        if (mPlatformInteractor == null || mErrorMessageConverter == null) {
            throw new NullPointerException("You must provide the implementation of ErrorMessage and PlatformInteractor.");
        }
    }

}