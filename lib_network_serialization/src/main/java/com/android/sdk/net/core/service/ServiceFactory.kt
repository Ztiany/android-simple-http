package com.android.sdk.net.core.service

import com.android.sdk.net.NetContext
import com.android.sdk.net.ServiceContext
import com.android.sdk.net.ServiceContextImpl
import com.android.sdk.net.core.json.ErrorJsonLenientConverterFactory
import com.android.sdk.net.core.json.GsonUtils
import com.android.sdk.net.core.progress.RequestProgressInterceptor
import com.android.sdk.net.core.progress.ResponseProgressInterceptor
import com.android.sdk.net.core.progress.UrlProgressListener
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.rxjava2.RxJavaChecker
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Ztiany
 */
class ServiceFactory internal constructor(
    private val hostFlag: String,
    private val httpClient: OkHttpClient,
    httpConfig: HttpConfig,
) {

    private val mBaseUrl: String = httpConfig.baseUrl()

    internal val mRetrofit: Retrofit

    init {
        val builder = Retrofit.Builder()

        if (!httpConfig.configRetrofit(httpClient, builder)) {
            builder
                .baseUrl(mBaseUrl)
                .client(httpClient)
                .addConverterFactory(ErrorJsonLenientConverterFactory(hostFlag, GsonConverterFactory.create(GsonUtils.gson())))

            if (RxJavaChecker.hasRxJava2()) {
                builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            }
        }

        mRetrofit = builder.build()
    }

    fun baseUrl(): String {
        return mBaseUrl
    }

    private fun checkIfDefaultHostFlag() {
        if (hostFlag != NetContext.DEFAULT_CONFIG) {
            throw IllegalArgumentException("ServiceFactory with hostFlag $hostFlag can't create default service. please use createServiceContext() instead.")
        }
    }

    fun <T> createDefault(clazz: Class<T>): T {
        checkIfDefaultHostFlag()
        return mRetrofit.create(clazz)
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
        val newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build()
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
        val newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build()
        return newRetrofit.create(clazz)
    }

    fun <T> createServiceContext(clazz: Class<T>): ServiceContext<T> {
        return ServiceContextImpl(hostFlag, mRetrofit.create(clazz))
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