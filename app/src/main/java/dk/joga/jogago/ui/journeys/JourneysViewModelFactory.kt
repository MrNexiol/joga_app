package dk.joga.jogago.ui.journeys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.joga.jogago.Repository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class JourneysViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JourneysViewModel::class.java)){
            return JourneysViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}