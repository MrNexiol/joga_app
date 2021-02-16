package com.prograils.joga.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.RefreshableSource
import com.prograils.joga.api.Resource

class CategoryViewModel(repository: Repository, token: String, id: String) : ViewModel() {
    val categoryClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return repository.getClasses(token, id)
        }
    }

    init {
        refreshData()
    }

    fun refreshData() {
        categoryClassesWrapper.refresh()
    }
}