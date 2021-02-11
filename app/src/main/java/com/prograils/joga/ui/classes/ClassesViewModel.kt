package com.prograils.joga.ui.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Category
import com.prograils.joga.api.Class
import com.prograils.joga.api.RefreshableSource
import com.prograils.joga.api.Resource

class ClassesViewModel(repository: Repository, token: String) : ViewModel() {
    val categoriesWrapper = object : RefreshableSource<List<Category>>() {
        override fun provideLiveData(): LiveData<Resource<List<Category>>> {
            return repository.getCategories(token)
        }
    }

    fun refreshClasses(){
        categoriesWrapper.refresh()
    }

    init {
        refreshClasses()
    }
}