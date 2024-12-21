package com.android.sdk.net.coroutines

import com.android.sdk.net.HostConfig
import com.android.sdk.net.HostConfigBuilder

/**
 * You can use this to set up a piece of logic that will be executed before retrial.
 */
fun HostConfigBuilder.coroutinesResultPostProcessor(resultPostProcessor: CoroutinesResultPostProcessor): HostConfigBuilder {
    registerComponent(CoroutinesResultPostProcessor::class.java, resultPostProcessor)
    return this
}

internal fun HostConfig.coroutinesResultPostProcessor() = getComponent(CoroutinesResultPostProcessor::class.java)
