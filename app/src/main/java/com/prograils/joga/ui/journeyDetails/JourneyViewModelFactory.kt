package com.prograils.joga.ui.journeyDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prograils.joga.Repository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class JourneyViewModelFactory(private val repository: Repository, private val id: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JourneyViewModel::class.java)){
            return JourneyViewModel(repository, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}