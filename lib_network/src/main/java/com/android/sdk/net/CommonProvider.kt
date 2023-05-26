package com.android.sdk.net

import com.android.sdk.net.core.provider.ErrorMessage
import com.android.sdk.net.core.provider.PlatformInteractor

internal interface CommonProvider {

    fun errorMessage(): ErrorMessage

    fun platformInteractor(): PlatformInteractor

}