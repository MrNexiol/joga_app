package dk.joga.jogago.ui.trainerDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class TrainerDetailViewModelFactory(private val trainerId: String, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainerDetailViewModel::class.java)){
            return TrainerDetailViewModel(trainerId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}