package com.prograils.joga.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Instructors
import com.prograils.joga.api.Journeys

class HomeViewModel(repository: Repository) : ViewModel() {
    val instructors: LiveData<Instructors> = repository.getInstructors()
    val journeys: LiveData<Journeys> = repository.getJourneys()
}