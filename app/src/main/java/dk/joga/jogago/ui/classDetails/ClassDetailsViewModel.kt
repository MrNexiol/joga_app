package dk.joga.jogago.ui.classDetails

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class ClassDetailsViewModel(private val id: String) : ViewModel() {
    val classWrapper = object : RefreshableSource<Class>() {
        override fun provideLiveData(): LiveData<Resource<Class>> {
            return AppContainer.repository.getClass(id)
        }
    }
    var isPlaying = false
    var player: SimpleExoPlayer? = null
    var nextClassId: String? = null
    var classIds: Array<String>? = null

    init {
        classWrapper.refresh()
    }

    fun toggleClassLike(){
        AppContainer.repository.toggleClassLike(id)
    }

    fun markAsWatched(){
        AppContainer.repository.markClassAsWatched(id)
    }

    fun initializePlayer(videoUrl: String, context: Context){
        if (player == null) {
            val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
            val hlsMediaSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(videoUrl))
            player = SimpleExoPlayer.Builder(context).build()
            player!!.setMediaSource(hlsMediaSource)
            player!!.prepare()
        }
    }

    fun showVideo(navController: NavController) {
        player!!.play()
        player!!.createMessage { _: Int, _: Any? ->
            markAsWatched()
        }
                .setPosition(0,20000)
                .send()
        player!!.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    markAsWatched()
                    if (nextClassId != null) {
                        val action = ClassDetailsFragmentDirections.actionClassDetailsFragmentSelf(nextClassId!!, classIds)
                        navController.navigate(action)
                    }
                }
            }
        })
    }
}