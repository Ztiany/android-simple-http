package com.android.sdk.net.core.service

import com.android.sdk.net.NetContext
import com.android.sdk.net.ServiceContext
import com.android.sdk.net.core.progress.RequestProgressInterceptor
import com.android.sdk.net.core.progress.ResponseProgressInterceptor
import com.android.sdk.net.core.progress.UrlProgressListener
import com.android.sdk.net.core.config.HttpConfig
import com.android.sdk.net.impl.ServiceContextImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author Ztiany
 */
class ServiceFactory internal constructor(
    private val hostFlag: String,
    private val httpClient: OkHttpClient,
    httpConfig: HttpConfig,
) {

    private val retrofit: Retrofit

    init {
        val builder = Retrofit.Builder().client(httpClient).apply { httpConfig.configRetrofit(this) }
        val listIterator = builder.converterFactories().listIterator()

        while (listIterator.hasNext()) {
            val factory = listIterator.next()
            if (factory !is ConversionLenientConverterFactory) {
                listIterator.set(ConversionLenientConverterFactory(hostFlag, factory))
            }
        }

        retrofit = builder.build()
    }

    private fun checkIfDefaultHostFlag() {
        if (hostFlag != NetContext.DEFAULT_CONFIG) {
            throw IllegalArgumentException("ServiceFactory with hostFlag $hostFlag can't create default service. please use createServiceContext() instead.")
        }
    }

    fun <T> createDefault(clazz: Class<T>): T {
        checkIfDefaultHostFlag()
        return retrofit.create(clazz)
    }

    fun <T> createDefaultWithUploadProgress(
        clazz: Class<T>,
        refreshTime: Int,
        urlProgressListener: UrlProgressListener,
    ): T {
        checkIfDefaultHostFlag()
        return createWithUploadProgress(clazz, refreshTime, urlProgressListener)
    }

    fun <T> createDefaultWithDownloadProgress(
        clazz: Class<T>,
        refreshTime: Int,
        urlProgressListener: UrlProgressListener,
    ): T {
        checkIfDefaultHostFlag()
        return createWithDownloadProgress(clazz, refreshTime, urlProgressListener)
    }

    private fun <T> createWithUploadProgress(
        clazz: Class<T>,
        refreshTime: Int,
        urlProgressListener: UrlProgressListener,
    ): T {
        val okHttpClient = httpClient
            .newBuilder()
            .addNetworkInterceptor(RequestProgressInterceptor(urlProgressListener, refreshTime))
            .build()
        val newRetrofit = retrofit.newBuilder().client(okHttpClient).build()
        return newRetrofit.create(clazz)
    }

    private fun <T> createWithDownloadProgress(
        clazz: Class<T>,
        refreshTime: Int,
        urlProgressListener: UrlProgressListener,
    ): T {
        val okHttpClient = httpClient
            .newBuilder()
            .addNetworkInterceptor(ResponseProgressInterceptor(urlProgressListener, refreshTime))
            .build()
        val newRetrofit = retrofit.newBuilder().client(okHttpClient).build()
        return newRetrofit.create(clazz)
    }

    fun <T> createServiceContext(clazz: Class<T>): ServiceContext<T> {
        return ServiceContextImpl(hostFlag, retrofit.create(clazz))
    }

    fun <T> createServiceContextWithUploadProgress(
        clazz: Class<T>,
        refreshTime: Int,
        urlProgressListener: UrlProgressListener,
    ): ServiceContext<T> {
        return ServiceContextImpl(hostFlag, createWithUploadProgress(clazz, refreshTime, urlProgressListener))
    }

    fun <T> createServiceContextWithDownloadProgress(
        clazz: Class<T>,
        refreshTime: Int,
        urlProgressListener: UrlProgressListener,
    ): ServiceContext<T> {
        return ServiceContextImpl(hostFlag, createWithDownloadProgress(clazz, refreshTime, urlProgressListener))
    }

}