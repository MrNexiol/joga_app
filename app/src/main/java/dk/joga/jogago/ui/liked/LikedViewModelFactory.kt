package dk.joga.jogago.ui.liked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.joga.jogago.Repository

@Suppress("UNCHECKED_CAST")
class LikedViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LikedViewModel::class.java)){
            return LikedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}