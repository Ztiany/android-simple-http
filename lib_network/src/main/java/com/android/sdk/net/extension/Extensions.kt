package com.android.sdk.net.extension

import android.content.Context
import com.android.sdk.net.CommonBuilder
import com.android.sdk.net.HostConfigBuilder
import com.android.sdk.net.NetContext
import com.android.sdk.net.ServiceContext
import com.android.sdk.net.core.service.ServiceFactory
import com.android.sdk.net.coroutines.CallResult
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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

inline fun <T, R> CallResult<T>.map(transform: (T) -> R): CallResult<R> {
    return when (this) {
        is CallResult.Success -> CallResult.Success(transform(data))
        is CallResult.Error -> CallResult.Error(error)
    }
}

//-opt-in=kotlin.RequiresOptIn
@OptIn(ExperimentalContracts::class)
fun <T> CallResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is CallResult.Success)
        returns(false) implies (this@isSuccess is CallResult.Error)
    }
    return this is CallResult.Success
}

@OptIn(ExperimentalContracts::class)
fun <T> CallResult<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is CallResult.Error)
        returns(false) implies (this@isError is CallResult.Success)
    }
    return this is CallResult.Error
}