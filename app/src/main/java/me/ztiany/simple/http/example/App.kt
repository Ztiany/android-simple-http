package me.ztiany.simple.http.example

import android.app.Application
import androidx.annotation.StringRes
import com.android.sdk.net.NetContext
import com.android.sdk.net.extension.addHostConfig
import com.android.sdk.net.extension.init
import com.android.sdk.net.extension.setDefaultHostConfig
import com.android.sdk.net.gson.GsonFactory
import com.google.gson.Gson
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
            httpConfig(newHttpConfig(gson))
            errorListener(newErrorHandler())
        }.addHostConfig("Mock") {
            httpConfig(newMockHttpConfig(gson))
            errorListener(newErrorHandler())
            errorBodyParser(newMockErrorBodyParser(gson))
        }
    }

    companion object {

        private var application: Application by Delegates.notNull()

        val gson: Gson = GsonFactory.newGson()

        fun getString(@StringRes id: Int, vararg args: Any): String {
            return application.getString(id, *args)
        }

    }

}