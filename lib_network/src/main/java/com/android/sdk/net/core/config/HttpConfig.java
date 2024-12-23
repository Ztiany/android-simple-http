package com.android.sdk.net.core.config;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author Ztiany
 */
public interface HttpConfig {

    void configHttp(@NonNull OkHttpClient.Builder builder);

    void configRetrofit(@NonNull Retrofit.Builder builder);

}