package me.ztiany.simple.http.example

import android.app.Application
import androidx.annotation.StringRes
import com.android.sdk.net.NetContext
import com.android.sdk.net.extension.addHostConfig
import com.android.sdk.net.extension.setDefaultHostConfig
import com.android.sdk.net.extension.init
import timber.log.Timber
import kotlin.properties.Delegates

class App : Application() {

    override fun onCreate() {
        application = this
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        NetContext.get().init(this) {
            errorMessageConverter(newErrorMessageConverter())
            platformInteractor(newPlatformInteractor())
        }.setDefaultHostConfig {
            httpConfig(newHttpConfig())
            errorListener(newErrorHandler())
            errorBodyParser(newErrorBodyParser())
            errorFactory { _, _ -> null }
        }.addHostConfig("Mock"){
            httpConfig(newMockHttpConfig())
            errorListener(newErrorHandler())
            errorBodyParser(newMockErrorBodyParser())
            errorFactory { _, _ -> null }
        }
    }

    companion object {

        private var application: Application by Delegates.notNull()

        fun getString(@StringRes id: Int, vararg args: Any): String {
            return application.getString(id, *args)
        }

    }

}