package dk.joga.jogago.ui.trainerDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.Instructor
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class TrainerDetailViewModel(trainerId: String) : ViewModel() {
    val instructorWrapper = object : RefreshableSource<Instructor>() {
        override fun provideLiveData(): LiveData<Resource<Instructor>> {
            return AppContainer.repository.getInstructor(trainerId)
        }
    }
    val instructorClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return AppContainer.repository.getInstructorClasses(trainerId)
        }
    }
    var playWhenReady: Boolean = true
    var isPlaying: Boolean = false
    var currentWindow: Int = 0
    var playbackPosition: Long = 0

    init {
        refreshData()
    }

    fun refreshData() {
        instructorClassesWrapper.refresh()
        instructorWrapper.refresh()
    }
}