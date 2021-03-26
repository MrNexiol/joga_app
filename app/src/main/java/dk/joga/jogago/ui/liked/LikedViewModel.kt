package dk.joga.jogago.ui.liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class LikedViewModel : ViewModel() {
    val likedClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return AppContainer.repository.getLikedClasses()
        }
    }

    init {
        refreshLikedClasses()
    }

    fun refreshLikedClasses() {
        likedClassesWrapper.refresh()
    }
}