package com.prograils.joga.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.Instructor
import com.prograils.joga.api.Journey
import com.prograils.joga.api.Resource

class HomeViewModel(repository: Repository, token: String) : ViewModel() {
    val dailyClass: LiveData<Resource<Class>> = repository.getDailyClass()
    val likedClasses: LiveData<Resource<List<Class>>> = repository.getLikedClasses(token)
    val journeys: LiveData<Resource<List<Journey>>> = repository.getJourneys()
    val instructors: LiveData<Resource<List<Instructor>>> = repository.getInstructors()
}