package me.ztiany.simple.http.example

import com.android.sdk.net.core.config.ErrorListener
import com.android.sdk.net.core.config.ErrorMessageConverter
import com.android.sdk.net.core.config.HttpConfig
import com.android.sdk.net.core.config.HttpExceptionHandler
import com.android.sdk.net.core.config.PlatformInteractor
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.blankj.utilcode.util.NetworkUtils
import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import me.ztiany.simple.http.example.App.Companion.getString
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit

internal fun newHttpConfig(gson: Gson): HttpConfig {

    return object : HttpConfig {

        private val CONNECTION_TIME_OUT = 30
        private val IO_TIME_OUT = 30

        override fun configRetrofit(builder: Retrofit.Builder) {
            builder
                .baseUrl("https://www.wanandroid.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        }

        override fun configHttp(builder: OkHttpClient.Builder) {
            // 常规配置
            builder
                .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
            configDebugIfNeeded(builder)
        }

        private fun configDebugIfNeeded(builder: OkHttpClient.Builder) {
            // 打印日志
            val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("===OkHttp===").i(message) }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(httpLoggingInterceptor)

            builder.authenticator { _, _ -> // 下面的 newApiHandler 中已经处理，这里不需要再处理了。
                // errorHandler.handleGlobalError(ApiHelper.buildAuthenticationExpiredException())
                null
            }
        }
    }

}

// fake response
private const val FAKE_BODY_NO_ENTITY = "{\"errorCode\":0,\"errorMsg\":\"消息\"}"
private const val FAKE_BODY_NO_ENTITY_2 = "{\"errorCode\":300,\"errorMsg\":\"我是一个错误的消息\",\"data\":{}}"
private const val FAKE_BODY_NULL_ENTITY = "{\"errorCode\":0,\"errorMsg\":\"消息\",\"data\":null}"
private const val FAKE_BODY_EMPTY_LIST = "{\"errorCode\":0,\"errorMsg\":\"消息\",\"data\":[]}"
private const val FAKE_BODY_EMPTY_ENTITY = "{\"errorCode\":0,\"errorMsg\":\"消息\",\"data\":{}}"
private const val FAKE_BODY_NUL = "null"

internal fun newMockHttpConfig(gson: Gson): HttpConfig {

    return object : HttpConfig {

        private val CONNECTION_TIME_OUT = 30
        private val IO_TIME_OUT = 30

        override fun configRetrofit(builder: Retrofit.Builder) {
            builder.baseUrl("https://www.wanandroid.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        }

        override fun configHttp(builder: OkHttpClient.Builder) {
            // 常规配置
            builder
                .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
            configDebugIfNeeded(builder)
        }

        private fun configDebugIfNeeded(builder: OkHttpClient.Builder) {
            // 打印日志
            val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("===OkHttp===").i(message) }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(httpLoggingInterceptor)

            // 模拟
            builder.addInterceptor {
                val request = it.request()
                val url = request.url.toString()
                val response = it.proceed(request)
                // response.newBuilder().code(200).message("OK").body(FAKE_BODY_NUL.toResponseBody())
                when {
                    url.contains("mock/parsingError") -> {
                        response.newBuilder()
                            .code(200)
                            .message("OK")
                            .body(FAKE_BODY_EMPTY_LIST.toResponseBody())
                            .build()
                    }

                    url.contains("mock/apiError") -> {
                        response.newBuilder()
                            .code(200)
                            .message("OK")
                            .body(FAKE_BODY_NO_ENTITY_2.toResponseBody())
                            .build()
                    }

                    url.contains("mock/httpError") -> {
                        response.newBuilder()
                            .code(455)
                            .message("Internal Error")
                            .body(FAKE_BODY_NO_ENTITY_2.toResponseBody())
                            .build()
                    }

                    else -> response
                }

            }

            builder.authenticator { _, _ -> // 下面的 newApiHandler 中已经处理，这里不需要再处理了。
                // errorHandler.handleGlobalError(ApiHelper.buildAuthenticationExpiredException())
                null
            }
        }
    }

}

fun newPlatformInteractor(): PlatformInteractor {
    return object : PlatformInteractor {
        override fun isConnected(): Boolean {
            return NetworkUtils.isConnected()
        }
    }
}

internal fun newMockErrorBodyParser(gson: Gson): HttpExceptionHandler {
    return object : HttpExceptionHandler {
        override fun handleException(httpException: HttpException, hostFlag: String): Throwable? {
            val body = httpException.response()?.errorBody()?.string() ?: return null
            Timber.d("parseErrorBody() called with: errorBody = $body, hostFlag = $hostFlag")
            val errorResult = gson.fromJson(body, MockErrorResult::class.java)
            return if (errorResult == null) {
                null
            } else {
                ApiErrorException(errorResult.code, errorResult.msg, hostFlag)
            }
        }
    }
}

internal fun newErrorMessageConverter(): ErrorMessageConverter {
    return object : ErrorMessageConverter {
        override fun convertWhenNetError(ioException: IOException): CharSequence {
            return getString(R.string.error_net_error)
        }

        override fun convertWhenParsingFailed(serverErrorException: ServerErrorException): CharSequence {
            return getString(R.string.error_service_data_error)
        }

        override fun convertWhenNoDataReturned(serverErrorException: ServerErrorException): CharSequence {
            return getString(R.string.error_service_no_data_error)
        }

        override fun convertWhenServerInternalError(httpException: HttpException): CharSequence {
            return getString(R.string.error_service_error)
        }

        override fun convertWhenClientRequestFailed(httpException: HttpException): CharSequence {
            return getString(R.string.error_request_error)
        }

        override fun convertWhenApiError(apiErrorException: ApiErrorException): CharSequence {
            return getString(R.string.error_api_code_mask_tips, apiErrorException.code)
        }

        override fun convertWhenUnknownError(exception: Throwable): CharSequence {
            return getString(R.string.error_unknown) + "：${exception.message}"
        }
    }
}

internal fun newErrorHandler() = object : ErrorListener {

    override fun onApiException(apiErrorException: ApiErrorException, hostFlag: String) {
        Timber.d("ApiHandler result: $apiErrorException, hostFlag: $hostFlag")
    }

    override fun onServerDataNotReturned(exception: ServerErrorException, hostFlag: String) {

    }

    override fun onParsingDataFailed(exception: ServerErrorException, hostFlag: String) {

    }

}