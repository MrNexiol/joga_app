package dk.joga.jogago.ui.classDetails

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Class
import dk.joga.jogago.api.RefreshableSource
import dk.joga.jogago.api.Resource

class ClassDetailsViewModel(private val id: String) : ViewModel() {
    private val playerListener = object : Player.EventListener {
        override fun onPlaybackStateChanged(state: Int) {
            if (state == Player.STATE_ENDED) {
                markAsWatched()
                if (nextClassId != null) {
                    val action = ClassDetailsFragmentDirections.actionClassDetailsFragmentSelf(nextClassId!!, classIds)
                    navController!!.navigate(action)
                }
            }
        }
    }

    val classWrapper = object : RefreshableSource<Class>() {
        override fun provideLiveData(): LiveData<Resource<Class>> {
            return AppContainer.repository.getClass(id)
        }
    }
    var navController: NavController? = null
    var isPlaying = false
    var playRemote = false
    var localPlayer: SimpleExoPlayer? = null
    var remotePlayer: CastPlayer? = null
    var castContext: CastContext? = null
    var castSession: CastSession? = null
    var classTitle = ""
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

    fun initializePlayers(videoUrl: String, context: Context){
        castSession = castContext!!.sessionManager.currentCastSession
        if (localPlayer == null) {
            val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
            val hlsMediaSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(videoUrl))
            localPlayer = SimpleExoPlayer.Builder(context).build()
            localPlayer!!.setMediaSource(hlsMediaSource)
            localPlayer!!.prepare()
        }
        if (remotePlayer == null) {
            val metaData = MediaMetadata.Builder().setTitle(classTitle).build()
            val mediaItem = MediaItem.Builder()
                    .setUri("https://github.com/mediaelement/mediaelement-files/blob/master/big_buck_bunny.mp4?raw=true")
                    .setMimeType(MimeTypes.VIDEO_MP4)
                    .setMediaMetadata(metaData)
                    .build()
            remotePlayer = CastPlayer(castContext!!)
            val mediaItemList: List<MediaItem> = listOf(mediaItem)
            if (remotePlayer!!.isCastSessionAvailable) {
                remotePlayer!!.addMediaItems(mediaItemList)
                remotePlayer!!.setMediaItem(mediaItem)
                remotePlayer!!.prepare()
                playRemote = true
            }
        }
    }

    fun showVideo() {
        if (playRemote) {
            remotePlayer!!.play()
            remotePlayer!!.addListener(playerListener)
        } else {
            localPlayer!!.play()
            localPlayer!!.createMessage { _, _ ->
                markAsWatched()
            }.setPosition(0,20000).send()
            localPlayer!!.addListener(playerListener)
        }
        isPlaying = true
    }

    override fun onCleared() {
        localPlayer!!.stop()
        remotePlayer!!.stop()
        localPlayer!!.release()
        remotePlayer!!.release()
    }
}