package dk.joga.jogago.ui.popup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.joga.jogago.Repository

@Suppress("UNCHECKED_CAST")
class PopupViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopupViewModel::class.java)){
            return PopupViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}