package com.android.sdk.net

import com.android.sdk.net.core.config.HttpExceptionHandler
import com.android.sdk.net.core.config.ErrorListener
import com.android.sdk.net.core.config.HttpConfig
import com.android.sdk.net.core.registry.ComponentRetriever
import com.android.sdk.net.core.registry.requireComponent

interface HostConfig : ComponentRetriever {

    fun errorListener(): ErrorListener? {
        return getComponent(ErrorListener::class.java)
    }

    fun httpConfig(): HttpConfig {
        return requireComponent(HttpConfig::class.java)
    }

    fun errorBodyHandler(): HttpExceptionHandler? {
        return getComponent(HttpExceptionHandler::class.java)
    }

}