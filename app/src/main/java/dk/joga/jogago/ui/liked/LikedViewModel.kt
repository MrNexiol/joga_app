package dk.joga.jogago.ui.liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.Repository
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class LikedViewModel(private val repository: Repository, private val token: String) : ViewModel() {
    val likedClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return repository.getLikedClasses(token)
        }
    }

    init {
        refreshLikedClasses()
    }

    fun refreshLikedClasses() {
        likedClassesWrapper.refresh()
    }
}