package com.android.sdk.net.rx2

import com.android.sdk.net.ServiceContext
import com.android.sdk.net.core.result.Result
import com.android.sdk.net.resultExtractor
import io.reactivex.Single

private class HttpResult<T>(
    override val data: T,
    override val code: Int,
    override val message: String,
) : Result<T> {

    override val isSuccess: Boolean
        get() = code == 200

}

private data class User(val name: String)

private interface TestAPI {
    fun getUserRx(): Single<HttpResult<User>>
}

private fun testRxJava(serviceContext: ServiceContext<TestAPI>) = with(serviceContext) {
    service.getUserRx()
        .resultExtractor()
        .subscribe(
            {

            },
            {

            }
        )
}
