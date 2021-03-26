package dk.joga.jogago.ui.classDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.Repository
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class ClassDetailsViewModel(private val repository: Repository, private val id: String) : ViewModel() {
    val classWrapper = object : RefreshableSource<Class>() {
        override fun provideLiveData(): LiveData<Resource<Class>> {
            return repository.getClass(id)
        }
    }
    var playWhenReady: Boolean = true
    var isPlaying: Boolean = false
    var currentWindow: Int = 0
    var playbackPosition: Long = 0

    init {
        classWrapper.refresh()
    }

    fun toggleClassLike(){
        repository.toggleClassLike(id)
    }

    fun markAsWatched(){
        repository.markClassAsWatched(id)
    }
}