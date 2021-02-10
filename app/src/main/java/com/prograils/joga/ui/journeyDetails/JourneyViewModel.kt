package com.prograils.joga.ui.journeyDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Journey
import com.prograils.joga.api.RefreshableSource
import com.prograils.joga.api.Resource

class JourneyViewModel(repository: Repository, token: String, id: String) : ViewModel() {
    val journeyWrapper = object : RefreshableSource<Journey>() {
        override fun provideLiveData(): LiveData<Resource<Journey>> {
            return repository.getJourney(token, id)
        }
    }

    init {
        refreshData()
    }

    fun refreshData() {
        journeyWrapper.refresh()
    }
}