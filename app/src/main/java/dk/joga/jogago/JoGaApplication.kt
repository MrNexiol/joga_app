package dk.joga.jogago

import android.app.Application

class JoGaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.init(this)
    }
}