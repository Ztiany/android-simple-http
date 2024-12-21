package com.android.sdk.net.core.registry

interface ComponentRegistrar {

    fun <T : Any> registerComponent(clazz: Class<T>, component: T)

    fun <T : Any> registerComponent(clazz: Class<T>, componentFactory: () -> T)

    fun putProperty(key: String, value: Any)

}