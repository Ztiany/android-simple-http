package com.android.sdk.net.coroutines

import com.android.sdk.net.ServiceContext
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.nonnull.apiCall
import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.android.sdk.net.coroutines.nullable.apiCallNullable
import com.android.sdk.net.coroutines.nullable.executeApiCallNullable
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
    val data1 = executeApiCall {
        testAPI.getData()
    }

    val data2 = executeApiCallNullable {
        testAPI.getDataNullable()
    }

    apiCall {
        testAPI.getData()
    } onSuccess {

    } onError {

    }

    apiCallNullable {
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