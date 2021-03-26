package dk.joga.jogago.ui.categoryDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class CategoryDetailsViewModelFactory(private val id: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryDetailsViewModel::class.java)){
            return CategoryDetailsViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}