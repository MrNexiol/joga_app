package dk.joga.jogago.ui.categoryDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class CategoryDetailsViewModel(id: String) : ViewModel() {
    var isMore = true
    var isLoading = false
    val categoryClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return AppContainer.repository.getCategoryClasses(id, page)
        }
    }

    init {
        refreshData()
    }

    private fun refreshData() {
        categoryClassesWrapper.refresh()
    }

    fun changePageNumber() {
        if (!isLoading) {
            categoryClassesWrapper.page++
            isLoading = true
            refreshData()
        }
    }

    fun resetData() {
        categoryClassesWrapper.page = 1
        isMore = true
        refreshData()
    }
}