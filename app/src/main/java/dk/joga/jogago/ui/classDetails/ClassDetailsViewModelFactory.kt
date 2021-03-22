package dk.joga.jogago.ui.classDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.joga.jogago.Repository

@Suppress("UNCHECKED_CAST")
class ClassDetailsViewModelFactory(private val repository: Repository, private val token: String, private val id: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClassDetailsViewModel::class.java)){
            return ClassDetailsViewModel(repository, token, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}