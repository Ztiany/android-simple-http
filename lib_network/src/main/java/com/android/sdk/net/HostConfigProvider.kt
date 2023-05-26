package com.android.sdk.net

import com.android.sdk.net.core.provider.ApiHandler
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor
import com.android.sdk.net.rxjava2.RxResultPostTransformer

internal interface HostConfigProvider {

    fun aipHandler(): ApiHandler?

    fun httpConfig(): HttpConfig

    fun exceptionFactory(): ExceptionFactory?

    fun coroutinesResultPostProcessor(): CoroutinesResultPostProcessor?

    fun rxResultPostTransformer(): RxResultPostTransformer<*>?

    fun errorBodyHandler(): ErrorBodyParser?

}