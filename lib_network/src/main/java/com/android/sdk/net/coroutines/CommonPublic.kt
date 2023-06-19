package com.android.sdk.net.coroutines

typealias RetryDeterminer = suspend (Int, Throwable) -> Boolean
