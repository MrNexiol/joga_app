package com.prograils.joga.ui.journeyDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Journey
import com.prograils.joga.api.Resource

class JourneyViewModel(repository: Repository, id: String) : ViewModel() {
    val journey: LiveData<Resource<Journey>> = repository.getJourney(id)
}