package com.android.sdk.net.coroutines

import com.android.sdk.net.ServiceContext
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.extension.coroutineMap
import com.android.sdk.net.extension.map
import com.android.sdk.net.extension.switchMap
import com.android.sdk.net.extension.zip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

private class HttpResult<T>(
    override val data: T,
    override val code: Int,
    override val message: String,
) : Result<T> {

    override val isSuccess: Boolean
        get() = code == 200

}

private data class User(val name: String)

private data class Friend(val name: String)

private data class Profile(val avatar: String)

private data class Address(val addressDetail: String)

private interface TestAPI {
    suspend fun getUser(): HttpResult<User>

    suspend fun getFriend(): HttpResult<Friend>

    suspend fun getUserProfile(user: User): HttpResult<Profile>

    suspend fun getUserAddress(user: User): HttpResult<Address>

    suspend fun getUserNullable(): HttpResult<User?>
}

private suspend fun testExecuteApiCall(testAPI: TestAPI) {
    val data1 = executeApiCall {
        testAPI.getUser()
    }

    val data2 = executeApiCallNullable {
        testAPI.getUserNullable()
    }
}

private suspend fun testCallResult(testAPI: TestAPI) {
    apiCall {
        testAPI.getUser()
    } onSuccess {

    } onError {

    }

    apiCall {
        testAPI.getUser()
    }.map {
        it.name
    } onSuccess {

    } onError {

    }

    apiCallNullable {
        testAPI.getUserNullable()
    }.map {
        it?.name
    } onError {

    } onSuccess {

    }

    apiCall {
        testAPI.getUser()
    }.switchMap {
        apiCall { testAPI.getUserProfile(it) }
    } onSuccess {

    } onError {

    }

    apiCall {
        testAPI.getUser()
    }.zip(apiCall { testAPI.getFriend() }) { user, friend ->
        user.name to friend.name
    } onSuccess {

    } onError {

    }

    apiCall {
        testAPI.getUser()
    }.coroutineMap {
        val profile = async(Dispatchers.IO) { apiCall { testAPI.getUserProfile(it) }.bind() }
        val address = async(Dispatchers.IO) { apiCall { testAPI.getUserAddress(it) }.bind() }
        profile.await() to address.await()
    } onSuccess {

    } onError {

    }
}

private suspend fun testServiceContext(serviceContext: ServiceContext<TestAPI>) {
    serviceContext.apiCall {
        getUser()
    }
}