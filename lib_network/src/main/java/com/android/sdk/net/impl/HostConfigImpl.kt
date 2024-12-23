package com.android.sdk.net.impl

import com.android.sdk.net.HostConfig
import com.android.sdk.net.core.config.HttpConfig
import com.android.sdk.net.core.registry.ComponentRegistrar

internal class HostConfigImpl(
    private val registrar: DefaultRegistrar = DefaultRegistrar(),
) : HostConfig, ComponentRegistrar by registrar {

    override fun getProperty(key: String): Any? {
        return registrar.getProperty(key)
    }

    override fun <T : Any> getComponent(clazz: Class<T>): T? {
        return registrar.getComponent(clazz)
    }

    fun checkRequired() {
        getComponent(HttpConfig::class.java) ?: throw NullPointerException("You must provide following a HttpConfig.")
    }

}