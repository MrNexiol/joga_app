package dk.joga.jogago.ui.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.joga.jogago.Repository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ClassesViewModelFactory(private val repository: Repository, private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClassesViewModel::class.java)){
            return ClassesViewModel(repository, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}