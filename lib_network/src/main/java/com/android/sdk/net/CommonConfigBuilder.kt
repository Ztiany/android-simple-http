package com.android.sdk.net

import androidx.annotation.MainThread
import com.android.sdk.net.core.provider.ErrorMessageConverter
import com.android.sdk.net.core.provider.PlatformInteractor

class CommonConfigBuilder internal constructor(private val netContext: NetContext) {

    private val commonProvider = CommonConfigImpl()

    fun errorMessageConverter(errorMessageConverter: ErrorMessageConverter): CommonConfigBuilder {
        commonProvider.mErrorMessageConverter = errorMessageConverter
        return this
    }

    fun platformInteractor(platformInteractor: PlatformInteractor): CommonConfigBuilder {
        commonProvider.mPlatformInteractor = platformInteractor
        return this
    }

    @MainThread
    internal fun setup(): NetContext {
        commonProvider.checkRequirement()
        netContext.initCommonProvider(commonProvider)
        return netContext
    }

}