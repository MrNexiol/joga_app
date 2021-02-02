package com.prograils.joga.ui.liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.Resource

class LikedViewModel(private val repository: Repository, private val token: String) : ViewModel() {
    private var likedClasses: LiveData<Resource<List<Class>>> = repository.getLikedClasses(token)

    fun getLikedClasses(): LiveData<Resource<List<Class>>>{
        refreshLikedClasses()
        return likedClasses
    }

    fun refreshLikedClasses(){
        likedClasses = repository.getLikedClasses(token)
    }
}