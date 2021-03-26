package dk.joga.jogago.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.Repository
import dk.joga.jogago.api.*

class HomeViewModel(private val repository: Repository) : ViewModel() {
    val dailyClassWrapper = object : RefreshableSource<Class>() {
        override fun provideLiveData(): LiveData<Resource<Class>> {
            return repository.getDailyClass()
        }
    }

    val newClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return repository.getNewClasses()
        }
    }

    val likedClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return repository.getLikedClasses()
        }
    }

    val journeysWrapper = object : RefreshableSource<List<Journey>>() {
        override fun provideLiveData(): LiveData<Resource<List<Journey>>> {
            return repository.getJourneys()
        }
    }

    val instructorsWrapper = object : RefreshableSource<List<Instructor>>() {
        override fun provideLiveData(): LiveData<Resource<List<Instructor>>> {
            return repository.getInstructors()
        }
    }

    init {
        refreshData()
    }

    fun refreshData(){
        dailyClassWrapper.refresh()
        newClassesWrapper.refresh()
        likedClassesWrapper.refresh()
        journeysWrapper.refresh()
        instructorsWrapper.refresh()
    }
}