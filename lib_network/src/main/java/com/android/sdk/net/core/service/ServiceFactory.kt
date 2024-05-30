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

    private val mRetrofit: Retrofit

    init {
        val builder = Retrofit.Builder()

        if (!httpConfig.configRetrofit(httpClient, builder)) {
            builder.baseUrl(mBaseUrl)
                .client(httpClient)
                .addConverterFactory(ErrorJsonLenientConverterFactory(GsonConverterFactory.create(GsonUtils.gson())))
            if (RxJavaChecker.hasRxJava2()) {
                builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            }
        }

        mRetrofit = builder.build()
    }

    fun baseUrl(): String {
        return mBaseUrl
    }

    private fun checkHostFlag() {
        if (hostFlag != NetContext.DEFAULT_CONFIG) {
            throw IllegalArgumentException("ServiceFactory with hostFlag=$hostFlag can't create default service. please use createServiceContext() instead.")
        }
    }

    fun <T> createDefault(clazz: Class<T>): T {
        checkHostFlag()
        return mRetrofit.create(clazz)
    }

    fun <T> createDefaultWithUploadProgress(clazz: Class<T>, urlProgressListener: UrlProgressListener): T {
        checkHostFlag()
        return createWithUploadProgress(urlProgressListener, clazz)
    }


    fun <T> createDefaultWithDownloadProgress(clazz: Class<T>, urlProgressListener: UrlProgressListener): T {
        checkHostFlag()
        return createWithDownloadProgress(urlProgressListener, clazz)
    }

    private fun <T> createWithUploadProgress(urlProgressListener: UrlProgressListener, clazz: Class<T>): T {
        val okHttpClient = httpClient
            .newBuilder()
            .addNetworkInterceptor(RequestProgressInterceptor(urlProgressListener))
            .build()
        val newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build()
        return newRetrofit.create(clazz)
    }

    private fun <T> createWithDownloadProgress(urlProgressListener: UrlProgressListener, clazz: Class<T>): T {
        val okHttpClient = httpClient
            .newBuilder()
            .addNetworkInterceptor(ResponseProgressInterceptor(urlProgressListener))
            .build()
        val newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build()
        return newRetrofit.create(clazz)
    }

    fun <T> createServiceContext(clazz: Class<T>): ServiceContext<T> {
        return ServiceContextImpl(hostFlag, mRetrofit.create(clazz))
    }

    fun <T> createServiceContextWithUploadProgress(clazz: Class<T>, urlProgressListener: UrlProgressListener): ServiceContext<T> {
        return ServiceContextImpl(hostFlag, createWithUploadProgress(urlProgressListener, clazz))
    }

    fun <T> createServiceContextWithDownloadProgress(clazz: Class<T>, urlProgressListener: UrlProgressListener): ServiceContext<T> {
        return ServiceContextImpl(hostFlag, createWithDownloadProgress(urlProgressListener, clazz))
    }

}