package com.prograils.joga.ui.journeys

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Journeys

class JourneysViewModel(repository: Repository) : ViewModel() {
    val journeys: LiveData<Journeys> = repository.getJourneys()
}