package com.android.sdk.net

import androidx.annotation.MainThread
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.ErrorListener
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.core.registry.ComponentRegistrar
import com.android.sdk.net.core.result.ApiErrorFactory
import com.android.sdk.net.impl.HostConfigImpl

class HostConfigBuilder internal constructor(
    private val hostFlag: String,
    private val netContext: NetContext,
) : ComponentRegistrar {

    private val hostConfigImpl = HostConfigImpl()

    fun errorListener(errorListener: ErrorListener): HostConfigBuilder {
        registerComponent(ErrorListener::class.java, errorListener)
        return this
    }

    fun httpConfig(httpConfig: HttpConfig): HostConfigBuilder {
        registerComponent(HttpConfig::class.java, httpConfig)
        return this
    }

    fun apiErrorFactory(apiErrorFactory: ApiErrorFactory): HostConfigBuilder {
        registerComponent(ApiErrorFactory::class.java, apiErrorFactory)
        return this
    }

    fun errorBodyParser(errorBodyParser: ErrorBodyParser): HostConfigBuilder {
        registerComponent(ErrorBodyParser::class.java, errorBodyParser)
        return this
    }

    @MainThread
    internal fun setup(): NetContext {
        hostConfigImpl.checkRequired()
        netContext.addInto(hostFlag, hostConfigImpl)
        return netContext
    }

    override fun <T : Any> registerComponent(clazz: Class<T>, component: T) {
        hostConfigImpl.registerComponent(clazz, component)
    }

    override fun <T : Any> registerComponent(clazz: Class<T>, componentFactory: () -> T) {
        hostConfigImpl.registerComponent(clazz, componentFactory)
    }

    override fun putProperty(key: String, value: Any) {
        hostConfigImpl.putProperty(key, value)
    }

}