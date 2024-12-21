package com.android.sdk.net.impl

import com.android.sdk.net.core.registry.ComponentRegistrar
import com.android.sdk.net.core.registry.ComponentRetriever

internal class DefaultRegistrar : ComponentRegistrar, ComponentRetriever {

    private val components = mutableMapOf<Class<*>, Any>()

    private val componentFactories = mutableMapOf<Class<*>, () -> Any>()

    private val properties = mutableMapOf<String, Any>()

    @Synchronized
    override fun <T : Any> registerComponent(clazz: Class<T>, component: T) {
        components[clazz] = component
    }

    @Synchronized
    override fun <T : Any> registerComponent(clazz: Class<T>, componentFactory: () -> T) {
        componentFactories[clazz] = componentFactory
    }

    @Synchronized
    override fun putProperty(key: String, value: Any) {
        properties[key] = value
    }

    @Synchronized
    override fun getProperty(key: String): Any? {
        return properties[key]
    }

    @Synchronized
    override fun <T : Any> getComponent(clazz: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return (components[clazz] ?: componentFactories[clazz]?.invoke()) as? T
    }

}