package me.ztiany.simple.http.example

import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.provider.ApiHandler
import com.android.sdk.net.core.provider.ErrorBodyParser
import com.android.sdk.net.core.provider.ErrorMessage
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.core.provider.PlatformInteractor
import com.blankj.utilcode.util.NetworkUtils
import me.ztiany.simple.http.example.App.Companion.getString
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit

internal fun newHttpConfig(): HttpConfig {

    return object : HttpConfig {

        private val CONNECTION_TIME_OUT = 30
        private val IO_TIME_OUT = 30

        override fun baseUrl() = "https://www.wanandroid.com/"

        override fun configRetrofit(
            okHttpClient: OkHttpClient, builder: Retrofit.Builder
        ): Boolean {
            return false
        }

        override fun configHttp(builder: OkHttpClient.Builder) {
            //常规配置
            builder
                .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
            configDebugIfNeeded(builder)
        }

        private fun configDebugIfNeeded(builder: OkHttpClient.Builder) {
            //打印日志
            val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("===OkHttp===").i(message) }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(httpLoggingInterceptor)

            builder.authenticator { _, _ -> //下面的 newApiHandler 中已经处理，这里不需要再处理了。
                //errorHandler.handleGlobalError(ApiHelper.buildAuthenticationExpiredException())
                null
            }
        }
    }

}

//假数据
private const val FAKE_BODY_NO_ENTITY = "{\"status\":0,\"msg\":\"消息\"}"
private const val FAKE_BODY_NO_ENTITY_2 = "{\"status\":30,\"message\":\"我是一个错误的消息\"}"
private const val FAKE_BODY_NULL_ENTITY = "{\"status\":0,\"msg\":\"消息\",\"data\":null}"
private const val FAKE_BODY_EMPTY_LIST = "{\"status\":0,\"msg\":\"消息\",\"data\":[]}"
private const val FAKE_BODY_EMPTY_ENTITY = "{\"status\":0,\"msg\":\"消息\",\"data\":{}}"
private const val FAKE_BODY_NUL = "null"

internal fun newMockHttpConfig(): HttpConfig {

    return object : HttpConfig {

        private val CONNECTION_TIME_OUT = 30
        private val IO_TIME_OUT = 30

        override fun baseUrl() = "https://www.wanandroid.com/"

        override fun configRetrofit(
            okHttpClient: OkHttpClient, builder: Retrofit.Builder
        ): Boolean {
            return false
        }

        override fun configHttp(builder: OkHttpClient.Builder) {
            //常规配置
            builder
                .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
            configDebugIfNeeded(builder)
        }

        private fun configDebugIfNeeded(builder: OkHttpClient.Builder) {
            //打印日志
            val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Timber.tag("===OkHttp===").i(message) }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(httpLoggingInterceptor)

            //模拟
            builder.addInterceptor {
                val response = it.proceed(it.request())
                //response.newBuilder().code(200).message("OK").body(FAKE_BODY_NUL.toResponseBody())
                response.newBuilder().code(455).message("Internal Error").body(FAKE_BODY_NO_ENTITY_2.toResponseBody())
                    .build()
            }

            builder.authenticator { _, _ -> //下面的 newApiHandler 中已经处理，这里不需要再处理了。
                //errorHandler.handleGlobalError(ApiHelper.buildAuthenticationExpiredException())
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

internal fun newErrorBodyParser(): ErrorBodyParser {
    return object : ErrorBodyParser {
        override fun parseErrorBody(errorBody: String, hostFlag: String): ApiErrorException? {
            val errorResult = JsonUtils.fromClass(errorBody, ErrorResult::class.java)
            return if (errorResult == null) {
                null
            } else {
                ApiErrorException(errorResult.code, errorResult.msg, hostFlag)
            }
        }
    }
}

internal fun newMockErrorBodyParser(): ErrorBodyParser {
    return object : ErrorBodyParser {
        override fun parseErrorBody(errorBody: String, hostFlag: String): ApiErrorException? {
            Timber.d("parseErrorBody() called with: errorBody = $errorBody, hostFlag = $hostFlag")
            val errorResult = JsonUtils.fromClass(errorBody, MockErrorResult::class.java)
            return if (errorResult == null) {
                null
            } else {
                ApiErrorException(errorResult.code, errorResult.msg, hostFlag)
            }
        }
    }
}

internal fun newErrorMessage(): ErrorMessage {
    return object : ErrorMessage {
        override fun netErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_net_error)
        }

        override fun serverDataErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_service_data_error)
        }

        override fun serverReturningNullEntityErrorMessage(exception: Throwable?): CharSequence {
            return getString(R.string.error_service_no_data_error)
        }

        override fun serverInternalErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_service_error)
        }

        override fun clientRequestErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_request_error)
        }

        override fun apiErrorMessage(exception: ApiErrorException): CharSequence {
            return getString(R.string.error_api_code_mask_tips, exception.code)
        }

        override fun unknownErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_unknown) + "：${exception.message}"
        }
    }
}

internal fun newApiHandler(): ApiHandler = ApiHandler { result ->
    //登录状态已过期，请重新登录、账号在其他设备登陆
    Timber.d("ApiHandler result: $result")
}