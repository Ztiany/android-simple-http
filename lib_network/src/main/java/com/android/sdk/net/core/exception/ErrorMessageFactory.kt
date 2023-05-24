package com.android.sdk.net.core.exception

interface ErrorMessageFactory {

    fun createMessage(exception: Throwable): CharSequence

}