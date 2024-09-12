package com.android.sdk.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ErrorListener;
import com.android.sdk.net.core.provider.ErrorBodyParser;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.core.result.ErrorFactory;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava2.RxResultPostTransformer;

final class HostConfigProviderImpl implements HostConfigProvider {

    ErrorFactory mErrorFactory;

    ErrorListener mErrorListener;

    HttpConfig mHttpConfig;

    RxResultPostTransformer<?> mRxResultPostTransformer;

    CoroutinesResultPostProcessor mCoroutinesResultPostProcessor;

    ErrorBodyParser mErrorBodyParser;

    @Nullable
    @Override
    public ErrorListener errorListener() {
        return mErrorListener;
    }

    @NonNull
    @Override
    public HttpConfig httpConfig() {
        return mHttpConfig;
    }

    @Nullable
    @Override
    public ErrorFactory errorFactory() {
        return mErrorFactory;
    }


    @Nullable
    @Override
    public CoroutinesResultPostProcessor coroutinesResultPostProcessor() {
        return mCoroutinesResultPostProcessor;
    }

    @Nullable
    @Override
    public RxResultPostTransformer<?> rxResultPostTransformer() {
        return mRxResultPostTransformer;
    }

    @Nullable
    @Override
    public ErrorBodyParser errorBodyHandler() {
        return mErrorBodyParser;
    }

    void checkRequired() {
        if (mHttpConfig == null) {
            throw new NullPointerException("You must provide following a HttpConfig.");
        }
    }

}