package com.android.sdk.net.core.registry

interface ComponentRetriever {

    fun getProperty(key: String): Any?

    fun <T : Any> getComponent(clazz: Class<T>): T?

}