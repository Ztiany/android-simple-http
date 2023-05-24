package com.android.sdk.net.coroutines

import com.android.sdk.net.SpecializedService
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.nonnull.apiCall
import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.android.sdk.net.coroutines.nullable.apiCallNullable
import com.android.sdk.net.coroutines.nullable.executeApiCallNullable
import com.android.sdk.net.rxjava2.resultExtractor
import io.reactivex.Single

private class HttpResult<T>(override val data: T, override val code: Int, override val message: String) : Result<T> {

    override val isSuccess: Boolean
        get() = code == 200

}

private data class User(val name: String)

private interface TestAPI {
    suspend fun getData(): HttpResult<User>

    suspend fun getDataRx(): Single<HttpResult<User>>

    suspend fun getDataNullable(): HttpResult<User?>
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

private suspend fun test2(specializedService: SpecializedService<TestAPI>) {
    specializedService.apiCall {
        getData()
    }
}

private suspend fun test3(specializedService: SpecializedService<TestAPI>) {
    specializedService.service
        .getDataRx()
        .resultExtractor()
        .subscribe(
            {

            },
            {

            }
        )
}