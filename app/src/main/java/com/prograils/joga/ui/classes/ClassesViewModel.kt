package com.prograils.joga.ui.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.RefreshableSource
import com.prograils.joga.api.Resource

class ClassesViewModel(repository: Repository, token: String) : ViewModel() {
    val classesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return repository.getClasses(token)
        }
    }

    init {
        classesWrapper.refresh()
    }
}