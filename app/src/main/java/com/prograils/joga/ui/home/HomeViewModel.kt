package com.prograils.joga.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.*

class HomeViewModel(private val repository: Repository, private val token: String) : ViewModel() {
    private var newClasses: LiveData<Resource<List<Class>>> = MutableLiveData()
    private var likedClasses: LiveData<Resource<List<Class>>> = MutableLiveData()
    private var journeys: LiveData<Resource<List<Journey>>> = MutableLiveData()
    private var instructors: LiveData<Resource<List<Instructor>>> = MutableLiveData()

    val dailyClassWrapper = object : RefreshableSource<Class>() {
        override fun provideLiveData(): LiveData<Resource<Class>> {
            return repository.getDailyClass(token)
        }
    }

    init {
        refreshDailyClass()
    }

    fun refreshDailyClass(){
        dailyClassWrapper.refresh()
    }

    fun getNewClasses(): LiveData<Resource<List<Class>>>{
        refreshNewClasses()
        return newClasses
    }

    fun refreshNewClasses(){
        newClasses = repository.getNewClasses(token)
    }

    fun getLikedClasses(): LiveData<Resource<List<Class>>>{
        refreshLikedClasses()
        return likedClasses
    }

    fun refreshLikedClasses(){
        likedClasses = repository.getLikedClasses(token)
    }

    fun getJourneys(): LiveData<Resource<List<Journey>>> {
        refreshJourneys()
        return journeys
    }

    fun refreshJourneys() {
        journeys = repository.getJourneys(token)
    }

    fun getInstructors(): LiveData<Resource<List<Instructor>>> {
        refreshInstructors()
        return instructors
    }

    fun refreshInstructors() {
        instructors = repository.getInstructors(token)
    }
}