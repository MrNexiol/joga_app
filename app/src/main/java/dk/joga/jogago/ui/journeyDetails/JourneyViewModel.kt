package dk.joga.jogago.ui.journeyDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Journey
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class JourneyViewModel(id: String) : ViewModel() {
    val journeyWrapper = object : RefreshableSource<Journey>() {
        override fun provideLiveData(): LiveData<Resource<Journey>> {
            return AppContainer.repository.getJourney(id)
        }
    }

    init {
        refreshData()
    }

    fun refreshData() {
        journeyWrapper.refresh()
    }
}