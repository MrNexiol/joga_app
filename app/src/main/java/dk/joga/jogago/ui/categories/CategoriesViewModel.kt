package dk.joga.jogago.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Category
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class CategoriesViewModel : ViewModel() {
    val categoriesWrapper = object : RefreshableSource<List<Category>>() {
        override fun provideLiveData(): LiveData<Resource<List<Category>>> {
            return AppContainer.repository.getCategories()
        }
    }

    fun refreshClasses(){
        categoriesWrapper.refresh()
    }

    init {
        refreshClasses()
    }
}