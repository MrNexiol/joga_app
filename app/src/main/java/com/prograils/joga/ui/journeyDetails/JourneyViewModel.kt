package com.prograils.joga.ui.journeyDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.JourneyResponse

class JourneyViewModel(repository: Repository, id: String) : ViewModel() {
    val journey: LiveData<JourneyResponse> = repository.getJourney(id)
}