package com.prograils.joga.ui.journeys

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Journey
import com.prograils.joga.api.RefreshableSource
import com.prograils.joga.api.Resource

class JourneysViewModel(repository: Repository, token: String) : ViewModel() {
    val journeysWrapper = object : RefreshableSource<List<Journey>>() {
        override fun provideLiveData(): LiveData<Resource<List<Journey>>> {
            return repository.getJourneys(token)
        }
    }

    fun refreshJourneys() {
        journeysWrapper.refresh()
    }

    init {
        refreshJourneys()
    }
}