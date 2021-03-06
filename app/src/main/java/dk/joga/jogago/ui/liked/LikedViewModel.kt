package dk.joga.jogago.ui.liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class LikedViewModel : ViewModel() {
    var isMore = true
    var isLoading = false
    val likedClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return AppContainer.repository.getLikedClasses(page)
        }
    }

    init {
        refreshData()
    }

    private fun refreshData() {
        likedClassesWrapper.refresh()
    }

    fun changePageNumber() {
        if (!isLoading) {
            likedClassesWrapper.page++
            isLoading = true
            refreshData()
        }
    }

    fun resetData() {
        likedClassesWrapper.page = 1
        isMore = true
        refreshData()
    }
}