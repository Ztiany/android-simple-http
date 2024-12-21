package com.android.sdk.net.core.service

import com.android.sdk.net.core.provider.HttpConfig
import okhttp3.OkHttpClient
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Ztiany
 */
internal class ServiceHelper {

    private val httpClientMap: MutableMap<String, OkHttpClient?> = ConcurrentHashMap()

    private val serviceFactoryMap: MutableMap<String, ServiceFactory> = ConcurrentHashMap()

    fun getOkHttpClient(flag: String, httpConfig: HttpConfig): OkHttpClient {
        var httpClient = httpClientMap[flag]
        if (httpClient == null) {
            val builder = OkHttpClient.Builder()
            httpConfig.configHttp(builder)
            httpClient = builder.build()
            httpClientMap[flag] = httpClient
        }
        return httpClient
    }

    fun getServiceFactory(flag: String, httpConfig: HttpConfig): ServiceFactory {
        var serviceFactory = serviceFactoryMap[flag]
        if (serviceFactory == null) {
            serviceFactory = ServiceFactory(flag, getOkHttpClient(flag, httpConfig), httpConfig)
            serviceFactoryMap[flag] = serviceFactory
        }
        return serviceFactory
    }

}