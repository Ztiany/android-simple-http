package com.android.sdk.net;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.provider.ErrorBodyParser;
import com.android.sdk.net.core.provider.HttpConfig;
import com.android.sdk.net.core.result.ExceptionFactory;
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor;
import com.android.sdk.net.rxjava2.RxResultPostTransformer;

public class HostConfigBuilder {

    private final HostConfigProviderImpl mConfigProvider = new HostConfigProviderImpl();

    private final NetContext mNetContext;

    private final String mFlag;

    HostConfigBuilder(@NonNull String flag, @NonNull NetContext netContext) {
        mNetContext = netContext;
        mFlag = flag;
    }

    public HostConfigBuilder aipHandler(@NonNull ApiHandler apiHandler) {
        mConfigProvider.mApiHandler = apiHandler;
        return this;
    }

    public HostConfigBuilder httpConfig(@NonNull HttpConfig httpConfig) {
        mConfigProvider.mHttpConfig = httpConfig;
        return this;
    }

    public HostConfigBuilder exceptionFactory(@NonNull ExceptionFactory exceptionFactory) {
        mConfigProvider.mExceptionFactory = exceptionFactory;
        return this;
    }

    public HostConfigBuilder errorBodyHandler(@NonNull ErrorBodyParser errorBodyParser) {
        mConfigProvider.mErrorBodyParser = errorBodyParser;
        return this;
    }

    /**
     * You can use this to set up a piece of logic that will be executed before retrial.
     */
    public HostConfigBuilder coroutinesResultPostProcessor(@Nullable CoroutinesResultPostProcessor resultPostProcessor) {
        mConfigProvider.mCoroutinesResultPostProcessor = resultPostProcessor;
        return this;
    }

    /**
     * If you use RxJava2, you can use this to set up a piece of logic that will be executed before retrial.
     */
    public HostConfigBuilder rx2ResultPostTransformer(@NonNull RxResultPostTransformer<?> resultPostProcessor) {
        mConfigProvider.mRxResultPostTransformer = resultPostProcessor;
        return this;
    }

    @MainThread
    public NetContext setUp() {
        mConfigProvider.checkRequired();
        mNetContext.addInto(mFlag, mConfigProvider);
        return mNetContext;
    }

}