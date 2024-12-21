package com.android.sdk.net

import com.android.sdk.net.core.provider.ErrorMessageConverter
import com.android.sdk.net.core.provider.PlatformInteractor

internal interface CommonConfig {

    fun errorMessageConverter(): ErrorMessageConverter

    fun platformInteractor(): PlatformInteractor

}