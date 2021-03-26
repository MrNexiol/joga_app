package dk.joga.jogago

import android.app.Application

class JoGaApplication : Application() {
    val appContainer = AppContainer(this)
}