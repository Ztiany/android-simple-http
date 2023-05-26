package com.android.sdk.net.extension

import android.content.Context
import com.android.sdk.net.CommonBuilder
import com.android.sdk.net.HostConfigBuilder
import com.android.sdk.net.NetContext
import com.android.sdk.net.ServiceContext
import com.android.sdk.net.core.service.ServiceFactory

inline fun <reified T> ServiceFactory.create(): T = create(T::class.java)

inline fun <reified T> ServiceFactory.createServiceContext(): ServiceContext<T> = createServiceContext(T::class.java)

fun NetContext.init(context: Context, init: CommonBuilder.() -> Unit): NetContext {
    val commonConfig = NetContext.get().newCommonConfig(context)
    init(commonConfig)
    commonConfig.setUp()
    return this
}

/** 添加以一个 host 配置，该配置的标识为 [NetContext.DEFAULT_CONFIG]。*/
fun NetContext.setDefaultHostConfig(config: HostConfigBuilder.() -> Unit): NetContext {
    val builder = NetContext.get().newHostBuilder(NetContext.DEFAULT_CONFIG)
    config(builder)
    builder.setUp()
    return this
}

/** 获取默认的 ServiceFactory（即配置标识为 [NetContext.DEFAULT_CONFIG]）。*/
fun NetContext.defaultServiceFactory(): ServiceFactory {
    return serviceFactory(NetContext.DEFAULT_CONFIG)
}

fun NetContext.addHostConfig(flag: String, config: HostConfigBuilder.() -> Unit): NetContext {
    val builder = NetContext.get().newHostBuilder(flag)
    config(builder)
    builder.setUp()
    return this
}