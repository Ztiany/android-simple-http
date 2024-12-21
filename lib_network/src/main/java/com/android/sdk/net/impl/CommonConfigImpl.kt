package com.android.sdk.net.impl

import com.android.sdk.net.CommonConfig
import com.android.sdk.net.core.provider.ErrorMessageConverter
import com.android.sdk.net.core.provider.PlatformInteractor
import com.android.sdk.net.core.registry.ComponentRegistrar

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
        if (getComponent(ErrorMessageConverter::class.java) == null || getComponent(PlatformInteractor::class.java) == null) {
            throw NullPointerException("You must provide the implementation of ErrorMessage and PlatformInteractor.")
        }
    }

}