package com.prograils.joga.ui.liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.RefreshableSource
import com.prograils.joga.api.Resource

class LikedViewModel(private val repository: Repository, private val token: String) : ViewModel() {
    val likedClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return repository.getLikedClasses(token)
        }
    }

    init {
        refreshLikedClasses()
    }

    private fun refreshLikedClasses() {
        likedClassesWrapper.refresh()
    }
}