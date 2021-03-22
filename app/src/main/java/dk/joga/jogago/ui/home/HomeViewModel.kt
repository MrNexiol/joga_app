package dk.joga.jogago.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.Repository
import dk.joga.jogago.api.*

class HomeViewModel(private val repository: Repository, private val token: String) : ViewModel() {
    val dailyClassWrapper = object : RefreshableSource<Class>() {
        override fun provideLiveData(): LiveData<Resource<Class>> {
            return repository.getDailyClass(token)
        }
    }

    val newClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return repository.getNewClasses(token)
        }
    }

    val likedClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return repository.getLikedClasses(token)
        }
    }

    val journeysWrapper = object : RefreshableSource<List<Journey>>() {
        override fun provideLiveData(): LiveData<Resource<List<Journey>>> {
            return repository.getJourneys(token)
        }
    }

    val instructorsWrapper = object : RefreshableSource<List<Instructor>>() {
        override fun provideLiveData(): LiveData<Resource<List<Instructor>>> {
            return repository.getInstructors(token)
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