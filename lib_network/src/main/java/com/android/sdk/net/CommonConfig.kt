package com.android.sdk.net

import com.android.sdk.net.core.provider.ErrorMessageConverter
import com.android.sdk.net.core.provider.PlatformInteractor
import com.android.sdk.net.core.registry.ComponentRetriever
import com.android.sdk.net.core.registry.requireComponent

interface CommonConfig : ComponentRetriever {

    fun errorMessageConverter(): ErrorMessageConverter = requireComponent(ErrorMessageConverter::class.java)

    fun platformInteractor(): PlatformInteractor = requireComponent(PlatformInteractor::class.java)

}