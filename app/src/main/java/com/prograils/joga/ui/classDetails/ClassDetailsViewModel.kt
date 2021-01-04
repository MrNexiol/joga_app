package com.prograils.joga.ui.classDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.Resource

class ClassDetailsViewModel(repository: Repository, id: String) : ViewModel() {
    val singleClass: LiveData<Resource<Class>> = repository.getClass(id)
}