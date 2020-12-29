package com.prograils.joga.ui.journeys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prograils.joga.Repository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class JourneysViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JourneysViewModel::class.java)){
            return JourneysViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}