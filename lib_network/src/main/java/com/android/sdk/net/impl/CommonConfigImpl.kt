package com.android.sdk.net.impl

import com.android.sdk.net.CommonConfig
import com.android.sdk.net.core.config.ErrorMessageConverter
import com.android.sdk.net.core.config.ErrorMessageFactory
import com.android.sdk.net.core.config.PlatformInteractor
import com.android.sdk.net.core.registry.ComponentRegistrar
import com.android.sdk.net.core.registry.requireComponent

internal class CommonConfigImpl(
    private val registrar: DefaultRegistrar = DefaultRegistrar(),
) : CommonConfig, ComponentRegistrar by registrar {

    override fun getProperty(key: String): Any? {
        return registrar.getProperty(key)
    }

    override fun <T : Any> getComponent(clazz: Class<T>): T? {
        return registrar.getComponent(clazz)
    }

    fun checkRequirement() {
        if (getComponent(PlatformInteractor::class.java) == null) {
            throw IllegalStateException("PlatformInteractor is required.")
        }
        val component = requireComponent(ErrorMessageFactory::class.java)
        if (component is ErrorMessageFactoryImpl && getComponent(ErrorMessageConverter::class.java) == null) {
            throw IllegalStateException("ErrorMessageConverter is required.")
        }
    }

}