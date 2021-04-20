package dk.joga.jogago.ui.trainerDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.gms.cast.framework.CastContext
import dk.joga.jogago.AppContainer
import dk.joga.jogago.PlayerManager
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.Instructor
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class TrainerDetailViewModel(trainerId: String, application: Application) : AndroidViewModel(application) {
    private var playerManager: PlayerManager? = null
    var isLoading = false

    val instructorWrapper = object : RefreshableSource<Instructor>() {
        override fun provideLiveData(): LiveData<Resource<Instructor>> {
            return AppContainer.repository.getInstructor(trainerId)
        }
    }
    val instructorClassesWrapper = object : RefreshableSource<List<Class>>() {
        override fun provideLiveData(): LiveData<Resource<List<Class>>> {
            return AppContainer.repository.getInstructorClasses(trainerId, page)
        }
    }

    init {
        refreshData()
    }

    private fun refreshData() {
        instructorClassesWrapper.refresh()
        instructorWrapper.refresh()
    }

    fun showVideo() {
        playerManager!!.startVideo()
    }

    fun isPlaying(): Boolean {
        return if (playerManager != null) {
            playerManager!!.isPlaying()
        } else {
            false
        }
    }

    override fun onCleared() {
        playerManager?.release()
    }

    fun initializePlayerManager(playerView: PlayerView, castContext: CastContext, videoUrl: String, classTitle: String) {
        if (playerManager == null) {
            playerManager = PlayerManager(playerView, getApplication(), castContext, videoUrl, classTitle)
        } else {
            playerManager!!.changePlayer(playerView)
        }
    }

    fun changePageNumber() {
        if (!isLoading) {
            instructorClassesWrapper.page++
            isLoading = true
            refreshData()
        }
    }

    fun resetData() {
        instructorClassesWrapper.page = 1
        refreshData()
    }

    fun pauseVideo() {
        playerManager?.pauseVideo()
    }
}