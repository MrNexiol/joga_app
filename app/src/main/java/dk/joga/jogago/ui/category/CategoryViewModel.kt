package dk.joga.jogago.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.Repository
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class CategoryViewModel(repository: Repository, token: String, id: String) : ViewModel() {
    val categoryClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return repository.getClasses(token, id)
        }
    }

    init {
        refreshData()
    }

    fun refreshData() {
        categoryClassesWrapper.refresh()
    }
}