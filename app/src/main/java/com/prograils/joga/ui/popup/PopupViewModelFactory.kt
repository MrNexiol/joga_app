package com.prograils.joga.ui.popup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prograils.joga.Repository

@Suppress("UNCHECKED_CAST")
class PopupViewModelFactory(private val repository: Repository, private val token: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopupViewModel::class.java)){
            return PopupViewModel(repository, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}