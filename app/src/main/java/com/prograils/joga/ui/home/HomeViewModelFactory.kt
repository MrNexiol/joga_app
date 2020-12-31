package com.prograils.joga.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prograils.joga.Repository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val repository: Repository, private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(repository, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}