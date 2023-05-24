package com.android.sdk.net.core.service

import com.android.sdk.net.core.json.ErrorJsonLenientConverterFactory
import com.android.sdk.net.core.json.GsonUtils
import com.android.sdk.net.core.progress.RequestProgressInterceptor
import com.android.sdk.net.core.progress.ResponseProgressInterceptor
import com.android.sdk.net.core.progress.UrlProgressListener
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.SpecializedService
import com.android.sdk.net.SpecializedServiceImpl
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
    httpConfig: HttpConfig
) {

    private val mBaseUrl: String
    private val mRetrofit: Retrofit

    init {
        mBaseUrl = httpConfig.baseUrl()
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

    fun <T> createWithUploadProgress(clazz: Class<T>, urlProgressListener: UrlProgressListener): T {
        val okHttpClient = httpClient
            .newBuilder()
            .addNetworkInterceptor(RequestProgressInterceptor(urlProgressListener))
            .build()
        val newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build()
        return newRetrofit.create(clazz)
    }

    fun <T> createWithDownloadProgress(clazz: Class<T>, urlProgressListener: UrlProgressListener): T {
        val okHttpClient = httpClient
            .newBuilder()
            .addNetworkInterceptor(ResponseProgressInterceptor(urlProgressListener))
            .build()
        val newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build()
        return newRetrofit.create(clazz)
    }

    fun baseUrl(): String {
        return mBaseUrl
    }

    fun <T> create(clazz: Class<T>): T {
        return mRetrofit.create(clazz)
    }

    fun <T> createSpecializedService(clazz: Class<T>): SpecializedService<T> {
        return SpecializedServiceImpl(hostFlag, create(clazz))
    }

    fun <T> createSpecializedServiceWithUploadProgress(clazz: Class<T>, urlProgressListener: UrlProgressListener): SpecializedService<T> {
        return SpecializedServiceImpl(hostFlag, createWithUploadProgress(clazz, urlProgressListener))
    }

    fun <T> createSpecializedServiceWithDownloadProgress(clazz: Class<T>, urlProgressListener: UrlProgressListener): SpecializedService<T> {
        return SpecializedServiceImpl(hostFlag, createWithDownloadProgress(clazz, urlProgressListener))
    }

}