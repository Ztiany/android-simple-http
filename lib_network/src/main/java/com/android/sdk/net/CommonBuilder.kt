package com.android.sdk.net

import androidx.annotation.MainThread
import com.android.sdk.net.core.provider.ErrorMessage
import com.android.sdk.net.core.provider.PlatformInteractor

class CommonBuilder internal constructor(private val netContext: NetContext) {

    private val commonProvider = CommonProviderImpl()

    fun errorMessage(errorMessage: ErrorMessage): CommonBuilder {
        commonProvider.mErrorMessage = errorMessage
        return this
    }

    fun platformInteractor(platformInteractor: PlatformInteractor): CommonBuilder {
        commonProvider.mPlatformInteractor = platformInteractor
        return this
    }

    @MainThread
    internal fun setUp(): NetContext {
        commonProvider.checkRequirement()
        netContext.initCommonProvider(commonProvider)
        return netContext
    }

}