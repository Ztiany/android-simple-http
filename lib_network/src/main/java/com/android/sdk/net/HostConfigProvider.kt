package com.android.sdk.net

import com.android.sdk.net.core.provider.ErrorListener
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.core.result.ErrorFactory
import com.android.sdk.net.coroutines.CoroutinesResultPostProcessor
import com.android.sdk.net.rxjava2.RxResultPostTransformer

internal interface HostConfigProvider {

    fun errorListener(): ErrorListener?

    fun httpConfig(): HttpConfig

    fun errorFactory(): ErrorFactory?

    fun coroutinesResultPostProcessor(): CoroutinesResultPostProcessor?

    fun rxResultPostTransformer(): RxResultPostTransformer<*>?

    fun errorBodyHandler(): ErrorBodyParser?

}