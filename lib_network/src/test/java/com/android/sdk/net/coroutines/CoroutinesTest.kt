package com.android.sdk.net.coroutines

import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.nonnull.apiCall
import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.android.sdk.net.coroutines.nullable.executeApiCallNullable

private class HttpResult<T>(
    override val data: T,
    override val code: Int,
    override val message: String
) : Result<T> {

    override val isSuccess: Boolean
        get() = code == 200

}

private data class User(val name: String)

private interface TestAPI {
    suspend fun getData(): HttpResult<User>

    suspend fun getDataNullable(): HttpResult<User?>

    suspend fun getData1(): CallResult<User>

}

private suspend fun testAPI(testAPI: TestAPI) {

    val data1 = executeApiCall {
        testAPI.getData()
    }

    val data2 = executeApiCallNullable {
        testAPI.getDataNullable()
    }

    apiCall { testAPI.getData() }
        .onSuccess {
            // do something
        } onError {
        // do something
    }

    with(apiCall { testAPI.getData() }) {
        onError {

        }
        onSuccess {

        }
    }

    when (val result = apiCall { testAPI.getData() }) {
        is CallResult.Success<User> -> {
            result.data
        }
        is CallResult.Error -> {
            result.error
        }
    }

}