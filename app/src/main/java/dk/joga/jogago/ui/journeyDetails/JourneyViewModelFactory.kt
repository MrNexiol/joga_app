package dk.joga.jogago.ui.journeyDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.joga.jogago.Repository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class JourneyViewModelFactory(private val repository: Repository, private val token: String, private val id: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JourneyViewModel::class.java)){
            return JourneyViewModel(repository, token, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}