package dk.joga.jogago.ui.classDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.gms.cast.framework.CastContext
import dk.joga.jogago.AppContainer
import dk.joga.jogago.PlayerManager
import dk.joga.jogago.api.Category
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class ClassDetailsViewModel(private val id: String, application: Application) : AndroidViewModel(application) {
    private var playerManager: PlayerManager? = null
    var classCategories: List<Category> = listOf()
    var startedVideo = false
    var wasPlayingDuringStop = false

    val classWrapper = object : RefreshableSource<Class>() {
        override fun provideLiveData(): LiveData<Resource<Class>> {
            return AppContainer.repository.getClass(id)
        }
    }
    var nextClassId: String? = null

    init {
        classWrapper.refresh()
    }

    fun showVideo() {
        playerManager!!.startVideo()
    }

    fun stopVideo() {
        playerManager?.stopVideo()
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
            playerManager = PlayerManager(
                classTitle,
                playerView,
                getApplication(),
                castContext,
                videoUrl
            )
            playerManager!!.setClassId(id)
        } else {
            playerManager!!.changePlayer(playerView)
        }
    }
}