package com.prograils.joga.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.Instructor
import com.prograils.joga.api.Journey
import com.prograils.joga.api.Resource

class HomeViewModel(private val repository: Repository, private val token: String) : ViewModel() {
    private var dailyClass: LiveData<Resource<Class>> = MutableLiveData()
    private var newClasses: LiveData<Resource<List<Class>>> = MutableLiveData()
    private var likedClasses: LiveData<Resource<List<Class>>> = MutableLiveData()
    private var journeys: LiveData<Resource<List<Journey>>> = MutableLiveData()
    private var instructors: LiveData<Resource<List<Instructor>>> = MutableLiveData()

    fun getDailyClass(): LiveData<Resource<Class>>{
        refreshDailyClass()
        return dailyClass
    }

    fun refreshDailyClass(){
        dailyClass = repository.getDailyClass(token)
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