package com.prograils.joga.ui.classDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prograils.joga.Repository

@Suppress("UNCHECKED_CAST")
class ClassDetailsViewModelFactory(private val repository: Repository, private val id: String, private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClassDetailsViewModel::class.java)){
            return ClassDetailsViewModel(repository, id, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}