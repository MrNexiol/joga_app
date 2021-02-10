package com.prograils.joga.ui.trainerDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.Instructor
import com.prograils.joga.api.RefreshableSource
import com.prograils.joga.api.Resource

class TrainerDetailViewModel(repository: Repository, trainerId: String, token: String) : ViewModel() {
    val instructorWrapper = object : RefreshableSource<Instructor>() {
        override fun provideLiveData(): LiveData<Resource<Instructor>> {
            return repository.getInstructor(token, trainerId)
        }
    }
    val instructorClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return repository.getInstructorClasses(token, trainerId)
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