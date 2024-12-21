package com.android.sdk.net

import androidx.annotation.MainThread
import com.android.sdk.net.core.provider.ErrorMessageConverter
import com.android.sdk.net.core.provider.PlatformInteractor
import com.android.sdk.net.core.registry.ComponentRegistrar
import com.android.sdk.net.impl.CommonConfigImpl

class CommonConfigBuilder internal constructor(private val netContext: NetContext) : ComponentRegistrar {

    private val commonConfigImpl = CommonConfigImpl()

    fun errorMessageConverter(errorMessageConverter: ErrorMessageConverter): CommonConfigBuilder {
        registerComponent(ErrorMessageConverter::class.java, errorMessageConverter)
        return this
    }

    fun platformInteractor(platformInteractor: PlatformInteractor): CommonConfigBuilder {
        registerComponent(PlatformInteractor::class.java, platformInteractor)
        return this
    }

    @MainThread
    internal fun setup(): NetContext {
        commonConfigImpl.checkRequirement()
        netContext.initCommonProvider(commonConfigImpl)
        return netContext
    }

    override fun <T : Any> registerComponent(clazz: Class<T>, component: T) {
        commonConfigImpl.registerComponent(clazz, component)
    }

    override fun <T : Any> registerComponent(clazz: Class<T>, componentFactory: () -> T) {
        commonConfigImpl.registerComponent(clazz, componentFactory)
    }

    override fun putProperty(key: String, value: Any) {
        commonConfigImpl.putProperty(key, value)
    }

}