package me.ztiany.simple.http.example

import android.app.Application
import androidx.annotation.StringRes
import com.android.sdk.net.NetContext
import com.android.sdk.net.extension.addHostConfig
import com.android.sdk.net.extension.init
import timber.log.Timber
import kotlin.properties.Delegates

class App : Application() {

    override fun onCreate() {
        application = this
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        NetContext.get().init(this) {
            errorMessage(newErrorMessage())
            platformInteractor(newPlatformInteractor())
        }.addHostConfig {
            httpConfig(newHttpConfig())
            aipHandler(newApiHandler())
            errorBodyHandler(newErrorBodyParser())
            exceptionFactory { _, _ -> null }
        }.addHostConfig("Mock"){
            httpConfig(newMockHttpConfig())
            aipHandler(newApiHandler())
            errorBodyHandler(newMockErrorBodyParser())
            exceptionFactory { _, _ -> null }
        }
    }

    companion object {

        private var application: Application by Delegates.notNull()

        fun getString(@StringRes id: Int, vararg args: Any): String {
            return application.getString(id, *args)
        }

    }

}