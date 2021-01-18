package com.prograils.joga.ui.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.Resource

class ClassesViewModel(repository: Repository, token: String) : ViewModel() {
    val classes: LiveData<Resource<List<Class>>> = repository.getClasses(token)
}