package com.prograils.joga.ui.classDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.Resource

class ClassDetailsViewModel(private val repository: Repository, private val token: String, private val id: String) : ViewModel() {
    val singleClass: LiveData<Resource<Class>> = repository.getClass(token, id)
    var playWhenReady: Boolean = true
    var isPlaying: Boolean = false
    var currentWindow: Int = 0
    var playbackPosition: Long = 0

    fun toggleClassLike(){
        repository.toggleClassLike(token, id)
    }

    fun markAsWatched(){
        repository.markClassAsWatched(token, id)
    }
}