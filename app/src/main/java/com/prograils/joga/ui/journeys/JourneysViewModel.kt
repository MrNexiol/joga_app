package com.prograils.joga.ui.journeys

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Journey
import com.prograils.joga.api.Resource

class JourneysViewModel(repository: Repository, token: String) : ViewModel() {
    val journeys: LiveData<Resource<List<Journey>>> = repository.getJourneys(token)
}