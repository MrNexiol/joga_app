package dk.joga.jogago

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.gms.cast.framework.CastContext
import com.google.firebase.analytics.ktx.logEvent

class PlayerManager(
    private var classTitle: String?,
    private var playerView: PlayerView,
    private val context: Context,
    castContext: CastContext,
    videoUrl: String
) : SessionAvailabilityListener, Player.EventListener {

    private var classId = ""
    private var classDuration = 0
    private var playbackPositionMs: Long = 0
    private var currentPlayer: Player? = null
    private var castPlayer: CastPlayer? = null
    private var localPlayer: SimpleExoPlayer? = null
    private var mediaItem: MediaItem? = null
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable { runnableTask() }

    init {
        if (classTitle == null) classTitle = "No title"
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        val hlsMediaSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(videoUrl))
        val metaData = MediaMetadata.Builder().setTitle(classTitle).build()
        mediaItem = MediaItem.Builder()
                .setUri(videoUrl)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .setMediaMetadata(metaData)
                .build()

        castPlayer = CastPlayer(castContext)
        castPlayer!!.setSessionAvailabilityListener(this)
        castPlayer!!.addListener(this)
        castPlayer!!.addMediaItem(mediaItem!!)

        localPlayer = SimpleExoPlayer.Builder(context).build()
        localPlayer!!.addListener(this)
        localPlayer!!.setMediaSource(hlsMediaSource)
        localPlayer!!.prepare()

        if (castPlayer!!.isCastSessionAvailable) {
            setCurrentPlayer(castPlayer!!)
        } else {
            setCurrentPlayer(localPlayer!!)
        }
    }

    override fun onCastSessionAvailable() {
        setCurrentPlayer(castPlayer!!)
    }

    override fun onCastSessionUnavailable() {
        setCurrentPlayer(localPlayer!!)
    }

    override fun onPlaybackStateChanged(state: Int) {
        if (state == Player.STATE_ENDED) {
            Toast.makeText(context, R.string.watched, Toast.LENGTH_SHORT).show()
            AppContainer.firebaseAnalytics.logEvent("video_finished") {
                param("name", classTitle!!)
                param("duration", classDuration.toLong())
            }
        }
    }

    private fun setCurrentPlayer(currentPlayer: Player) {
        if (this.currentPlayer == currentPlayer) {
            return
        }

        playbackPositionMs = C.TIME_UNSET
        var windowIndex = C.INDEX_UNSET
        var playWhenReady = false
        var isPlaying = false

        if (this.currentPlayer != null) {
            val playbackState = this.currentPlayer!!.playbackState
            if (playbackState != Player.STATE_ENDED) {
                playbackPositionMs = this.currentPlayer!!.currentPosition
                windowIndex = this.currentPlayer!!.currentWindowIndex
                playWhenReady = this.currentPlayer!!.playWhenReady
                isPlaying = isPlaying()
            }
            this.currentPlayer!!.stop()
        }

        this.currentPlayer = currentPlayer
        this.playerView.player = currentPlayer

        if (windowIndex != C.INDEX_UNSET) {
            this.currentPlayer!!.playWhenReady = playWhenReady
            this.currentPlayer!!.seekTo(playbackPositionMs)
        }

        if (isPlaying) {
            startVideo()
        }
    }

    fun startVideo() {
        if (currentPlayer == castPlayer) {
            (currentPlayer as CastPlayer).setMediaItem(mediaItem!!)
        }
        currentPlayer!!.prepare()
        currentPlayer!!.play()
        if (classId.isNotEmpty()) {
            handler.postDelayed(runnable, 5000)
        }
    }

    fun stopVideo() {
        currentPlayer?.pause()
    }

    fun isPlaying(): Boolean {
        return if (currentPlayer != null) {
            currentPlayer!!.isPlaying
        } else {
            false
        }
    }

    fun changePlayer(playerView: PlayerView) {
        this.playerView = playerView
        this.playerView.player = currentPlayer
    }

    private fun removeHandlerCallback() {
        handler.removeCallbacks(runnable)
    }

    private fun runnableTask() {
        if (currentPlayer!!.currentPosition > 20000) {
            AppContainer.repository.markClassAsWatched(classId)
            AppContainer.firebaseAnalytics.logEvent("video_watched") {
                param("name", classTitle!!)
                param("duration", classDuration.toLong())
            }
            removeHandlerCallback()
        } else {
            handler.postDelayed(runnable, 5000)
        }
    }

    fun setClassId(id: String) {
        this.classId = id
    }

    fun setClassDuration(duration: Int) {
        this.classDuration = duration
    }

    fun release() {
        playerView.player = null
        currentPlayer?.stop()
        currentPlayer?.release()
        localPlayer?.stop()
        localPlayer?.release()
        castPlayer?.stop()
        castPlayer?.release()
    }
}