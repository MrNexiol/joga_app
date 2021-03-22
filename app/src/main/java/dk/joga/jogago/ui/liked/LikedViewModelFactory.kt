package dk.joga.jogago.ui.liked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.joga.jogago.Repository

@Suppress("UNCHECKED_CAST")
class LikedViewModelFactory(private val repository: Repository, private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LikedViewModel::class.java)){
            return LikedViewModel(repository, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}