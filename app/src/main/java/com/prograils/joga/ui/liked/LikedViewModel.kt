package com.prograils.joga.ui.liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.Resource

class LikedViewModel(repository: Repository, token: String) : ViewModel() {
    val likedClasses: LiveData<Resource<List<Class>>> = repository.getLikedClasses(token)
}