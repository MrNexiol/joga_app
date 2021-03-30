package dk.joga.jogago.ui.categoryDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class CategoryDetailsViewModel(id: String) : ViewModel() {
    val categoryClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return AppContainer.repository.getCategory(id)
        }
    }

    init {
        refreshData()
    }

    fun refreshData() {
        categoryClassesWrapper.refresh()
    }
}