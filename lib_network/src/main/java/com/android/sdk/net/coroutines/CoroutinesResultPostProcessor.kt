package com.android.sdk.net.coroutines

/**
 *@author Ztiany
 */
interface CoroutinesResultPostProcessor {

    suspend fun retry(throwable: Throwable): Boolean

}