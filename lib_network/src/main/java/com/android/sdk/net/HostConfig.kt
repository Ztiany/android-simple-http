package com.android.sdk.net

import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.ErrorListener
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.core.result.ApiErrorFactory
import com.android.sdk.net.core.registry.ComponentRetriever
import com.android.sdk.net.core.registry.requireComponent

interface HostConfig : ComponentRetriever {

    fun errorListener(): ErrorListener? {
        return getComponent(ErrorListener::class.java)
    }

    fun httpConfig(): HttpConfig {
        return requireComponent(HttpConfig::class.java)
    }

    fun apiErrorFactory(): ApiErrorFactory? {
        return getComponent(ApiErrorFactory::class.java)
    }

    fun errorBodyHandler(): ErrorBodyParser? {
        return getComponent(ErrorBodyParser::class.java)
    }

}