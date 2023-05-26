package com.android.sdk.net

import androidx.annotation.MainThread
import com.android.sdk.net.core.provider.ApiHandler
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor
import com.android.sdk.net.rxjava2.RxResultPostTransformer

class HostConfigBuilder internal constructor(
    private val hostFlag: String,
    private val netContext: NetContext
) {

    private val configProvider = HostConfigProviderImpl()

    fun aipHandler(apiHandler: ApiHandler): HostConfigBuilder {
        configProvider.mApiHandler = apiHandler
        return this
    }

    fun httpConfig(httpConfig: HttpConfig): HostConfigBuilder {
        configProvider.mHttpConfig = httpConfig
        return this
    }

    fun exceptionFactory(exceptionFactory: ExceptionFactory): HostConfigBuilder {
        configProvider.mExceptionFactory = exceptionFactory
        return this
    }

    fun errorBodyHandler(errorBodyParser: ErrorBodyParser): HostConfigBuilder {
        configProvider.mErrorBodyParser = errorBodyParser
        return this
    }

    /**
     * You can use this to set up a piece of logic that will be executed before retrial.
     */
    fun coroutinesResultPostProcessor(resultPostProcessor: CoroutinesResultPostProcessor): HostConfigBuilder {
        configProvider.mCoroutinesResultPostProcessor = resultPostProcessor
        return this
    }

    /**
     * If you use RxJava2, you can use this to set up a piece of logic that will be executed before retrial.
     */
    fun rx2ResultPostTransformer(resultPostProcessor: RxResultPostTransformer<*>): HostConfigBuilder {
        configProvider.mRxResultPostTransformer = resultPostProcessor
        return this
    }

    @MainThread
    internal fun setUp(): NetContext {
        configProvider.checkRequired()
        netContext.addInto(hostFlag, configProvider)
        return netContext
    }

}