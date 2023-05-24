package com.android.sdk.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.provider.ErrorBodyParser;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.core.result.ExceptionFactory;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava2.RxResultPostTransformer;

public interface HostConfigProvider {

    @Nullable
    ApiHandler aipHandler();

    @NonNull
    HttpConfig httpConfig();

    @Nullable
    ExceptionFactory exceptionFactory();

    @Nullable
    CoroutinesResultPostProcessor coroutinesResultPostProcessor();

    @Nullable
    RxResultPostTransformer<?> rxResultPostTransformer();

    @Nullable
    ErrorBodyParser errorBodyHandler();

}
