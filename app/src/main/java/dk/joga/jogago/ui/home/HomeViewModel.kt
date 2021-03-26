package dk.joga.jogago.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.*

class HomeViewModel() : ViewModel() {
    val dailyClassWrapper = object : RefreshableSource<Class>() {
        override fun provideLiveData(): LiveData<Resource<Class>> {
            return AppContainer.repository.getDailyClass()
        }
    }

    val newClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return AppContainer.repository.getNewClasses()
        }
    }

    val likedClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return AppContainer.repository.getLikedClasses()
        }
    }

    val journeysWrapper = object : RefreshableSource<List<Journey>>() {
        override fun provideLiveData(): LiveData<Resource<List<Journey>>> {
            return AppContainer.repository.getJourneys()
        }
    }

    val instructorsWrapper = object : RefreshableSource<List<Instructor>>() {
        override fun provideLiveData(): LiveData<Resource<List<Instructor>>> {
            return AppContainer.repository.getInstructors()
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