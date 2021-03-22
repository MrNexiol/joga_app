package dk.joga.jogago.ui.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.Repository
import dk.joga.jogago.api.Category
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class ClassesViewModel(repository: Repository, token: String) : ViewModel() {
    val categoriesWrapper = object : RefreshableSource<List<Category>>() {
        override fun provideLiveData(): LiveData<Resource<List<Category>>> {
            return repository.getCategories(token)
        }
    }

    fun refreshClasses(){
        categoriesWrapper.refresh()
    }

    init {
        refreshClasses()
    }
}