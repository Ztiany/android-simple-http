package com.android.sdk.net.rxjava2

import com.android.sdk.net.HostConfig
import com.android.sdk.net.HostConfigBuilder

/**
 * If you use RxJava2, you can use this to set up a piece of logic that will be executed before retrial.
 */
fun HostConfigBuilder.rx2ResultPostTransformer(resultPostProcessor: RxResultPostTransformer<*>): HostConfigBuilder {
    registerComponent(RxResultPostTransformer::class.java, resultPostProcessor)
    return this
}

internal fun HostConfig.rxResultPostTransformer(): RxResultPostTransformer<*>? {
    return getComponent(RxResultPostTransformer::class.java)
}
