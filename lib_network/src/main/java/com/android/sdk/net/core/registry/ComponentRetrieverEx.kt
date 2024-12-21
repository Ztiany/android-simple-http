package com.android.sdk.net.core.registry

fun <T> ComponentRetriever.requireComponent(clazz: Class<T>): T {
    return getComponent(clazz) ?: throw IllegalStateException("Component not found: $clazz")
}

fun ComponentRetriever.requireProperty(key: String): Any {
    return getProperty(key) ?: throw IllegalStateException("Property not found: $key")
}