package com.android.sdk.net.impl

import com.android.sdk.net.core.registry.ComponentRegistrar
import com.android.sdk.net.core.registry.ComponentRetriever
import java.util.concurrent.ConcurrentHashMap

internal class DefaultRegistrar : ComponentRegistrar, ComponentRetriever {

    private val components = ConcurrentHashMap<Class<*>, Any>()

    private val componentFactories = ConcurrentHashMap<Class<*>, () -> Any>()

    private val properties = ConcurrentHashMap<String, Any>()

    override fun <T : Any> registerComponent(clazz: Class<T>, component: T) {
        components[clazz] = component
    }

    override fun <T : Any> registerComponent(clazz: Class<T>, componentFactory: () -> T) {
        componentFactories[clazz] = componentFactory
    }

    override fun <T : Any> getComponent(clazz: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return (components[clazz] ?: componentFactories[clazz]?.invoke()) as? T
    }

    override fun putProperty(key: String, value: Any) {
        properties[key] = value
    }

    override fun getProperty(key: String): Any? {
        return properties[key]
    }

}