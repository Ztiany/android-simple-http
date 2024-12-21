package com.android.sdk.net

import com.android.sdk.net.core.provider.ErrorListener
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.core.result.ApiErrorFactory
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor
import com.android.sdk.net.rxjava2.RxResultPostTransformer

internal interface HostConfig {

    fun errorListener(): ErrorListener?

    fun httpConfig(): HttpConfig

    fun apiErrorFactory(): ApiErrorFactory?

    fun coroutinesResultPostProcessor(): CoroutinesResultPostProcessor?

    fun rxResultPostTransformer(): RxResultPostTransformer<*>?

    fun errorBodyHandler(): ErrorBodyParser?

}