package dk.joga.jogago.ui.journeys

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Journey
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class JourneysViewModel : ViewModel() {
    val journeysWrapper = object : RefreshableSource<List<Journey>>() {
        override fun provideLiveData(): LiveData<Resource<List<Journey>>> {
            return AppContainer.repository.getJourneys()
        }
    }

    fun refreshJourneys() {
        journeysWrapper.refresh()
    }

    init {
        refreshJourneys()
    }
}