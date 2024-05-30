package com.android.sdk.net.coroutines

import com.android.sdk.net.ServiceContext
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.nonnull.internalApiCall
import com.android.sdk.net.coroutines.nonnull.internalExecuteApiCall
import com.android.sdk.net.coroutines.nullable.internalApiCallNullable
import com.android.sdk.net.coroutines.nullable.internalExecuteApiCallNullable
import com.android.sdk.net.extension.map
import io.reactivex.Single

private class HttpResult<T>(override val data: T, override val code: Int, override val message: String) : Result<T> {

    override val isSuccess: Boolean
        get() = code == 200

}

private data class User(val name: String)

private interface TestAPI {
    suspend fun getData(): HttpResult<User>

    suspend fun getDataNullable(): HttpResult<User?>

    fun getDataRx(): Single<HttpResult<User>>
}

private suspend fun test1(testAPI: TestAPI) {
    val data1 = internalExecuteApiCall {
        testAPI.getData()
    }

    val data2 = internalExecuteApiCallNullable {
        testAPI.getDataNullable()
    }

    internalApiCall {
        testAPI.getData()
    } onSuccess {

    } onError {

    }

    internalApiCall {
        testAPI.getData()
    }.map {
        it.name
    } onSuccess {

    } onError {

    }

    internalApiCallNullable {
        testAPI.getData()
    } onError {

    } onSuccess {

    }

    internalApiCallNullable {
        testAPI.getData()
    }.map {
        it?.name
    } onError {

    } onSuccess {

    }

    internalApiCallNullable {
        testAPI.getData()
    } onError {

    } onSuccess {

    }

    internalApiCallNullable {
        testAPI.getDataNullable()
    } onError {

    } onSuccess {

    }
}

private suspend fun test2(serviceContext: ServiceContext<TestAPI>) {
    serviceContext.apiCall {
        getData()
    }
}

private fun test3(serviceContext: ServiceContext<TestAPI>) {
    with(serviceContext) {
        service.getDataRx()
            .resultExtractor()
            .subscribe(
                {

                },
                {

                }
            )
    }
}