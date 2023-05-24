package com.android.sdk.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ErrorBodyParser;
import com.android.sdk.net.core.provider.ErrorMessage;
import com.android.sdk.net.core.provider.PlatformInteractor;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava2.RxResultPostTransformer;

final class CommonProviderImpl implements CommonProvider {

    ErrorMessage mErrorMessage;

    PlatformInteractor mPlatformInteractor;

    @NonNull
    @Override
    public ErrorMessage errorMessage() {
        return mErrorMessage;
    }

    @NonNull
    @Override
    public PlatformInteractor platformInteractor() {
        return mPlatformInteractor;
    }

    public void checkRequirement() {
        if (mPlatformInteractor == null || mErrorMessage == null) {
            throw new NullPointerException("You must provide the implementation of ErrorMessage and PlatformInteractor.");
        }
    }

}