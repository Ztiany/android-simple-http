package com.android.sdk.net.coroutines

typealias RetryDeterminer = (Int, Throwable) -> Boolean
