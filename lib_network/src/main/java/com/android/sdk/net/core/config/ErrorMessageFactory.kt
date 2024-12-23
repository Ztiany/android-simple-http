package com.android.sdk.net.core.config

interface ErrorMessageFactory {

    fun convert(exception: Throwable): CharSequence

}