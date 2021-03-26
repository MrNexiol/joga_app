package dk.joga.jogago

import android.app.Application

class JoGaApplication : Application() {
    val appContainer = AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer.init(this)
    }
}