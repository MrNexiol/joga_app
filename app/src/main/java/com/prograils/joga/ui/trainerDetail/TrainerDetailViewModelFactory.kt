package com.prograils.joga.ui.trainerDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prograils.joga.Repository

@Suppress("UNCHECKED_CAST")
class TrainerDetailViewModelFactory(private val repository: Repository, private val trainerId: String, private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainerDetailViewModel::class.java)){
            return TrainerDetailViewModel(repository, trainerId, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}