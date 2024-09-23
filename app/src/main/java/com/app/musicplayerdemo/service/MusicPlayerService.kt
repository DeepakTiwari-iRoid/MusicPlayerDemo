package com.app.musicplayerdemo.service


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.app.musicplayerdemo.utils.Constants.BG_MUSIC_VOL
import com.app.musicplayerdemo.utils.Constants.MEDIA_URIS
import com.app.musicplayerdemo.utils.Constants.MUTE_NON_PRIME_BG_MUSIC
import com.app.musicplayerdemo.utils.Constants.PRIM_VOLUME

const val TAG = "MSessionService"

class MusicPlayerService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer

    //    private lateinit var playerNarrators: ExoPlayer
    private val playersBackground: MutableMap<String, ExoPlayer> = mutableMapOf()

    override fun onCreate() {
        super.onCreate()

        player = createPlayer(this, true)
        //playerNarrators = createPlayer(this)
        // Configure media session for video playback as well
        mediaSession = MediaSession.Builder(this, player).build()
        synchronizedMainPlayers()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { handleIntents(it) }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    // The user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!

        if (player.playWhenReady) {
            player.pause()
        }

        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) {
            // Stop the service if not playing, continue playing in the background
            // otherwise.
            stopSelf()
        }
    }

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            cleanUpBgPlayers()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }


    /**________________________METHODS_________________________________*/
//volume controlling seek bar ( put in task tomorrow in task )
//mute audio by index

    private fun createPlayer(context: Context, isMainPlayer: Boolean = false): ExoPlayer {

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        return ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, isMainPlayer)
            .setHandleAudioBecomingNoisy(true)
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .build()
    }

    private fun addBackGroundMusics(mediaUrls: List<String>, muteItems: ArrayList<String>? = null) {
        mediaUrls.map {
            val player = createPlayer(this)  // Create a new player
            val mediaItem = MediaItem.fromUri(it)  // Create a media item from URI
            player.setMediaItem(mediaItem)  // Add media item to player
            player.prepare()  // Prepare the player
            player.repeatMode = Player.REPEAT_MODE_ONE
            playersBackground[it] = player  // Store the player in the map
        }

        if (!muteItems.isNullOrEmpty()) {
            muteItems.map { playersBackground[it]?.volume = 0f }
        }

        Log.i(TAG, "addBackGroundMusics: ${playersBackground.keys}")
    }


    private fun handleIntents(intent: Intent) {
        when (intent.action) {
            MEDIA_URIS -> {
                val mediaUrls = intent.getStringArrayListExtra(MEDIA_URIS)
                val muteAudioIndex = intent.getStringArrayListExtra(MUTE_NON_PRIME_BG_MUSIC)

                mediaUrls?.let { urls ->
                    cleanUpBgPlayers()
                    addBackGroundMusics(urls, muteAudioIndex)
                    synchronizedBGPlayers()
                }
                Log.d(TAG, "handleIntents: MEDIA_URIS")
            }

            PRIM_VOLUME -> {
                val primeVolume = intent.getFloatExtra(PRIM_VOLUME, 1f)
                player.volume = primeVolume
//                playersBackground.map { it.value.volume = prime_volume }
            }

            BG_MUSIC_VOL -> {
                val muteItems = intent.getStringArrayListExtra(MUTE_NON_PRIME_BG_MUSIC)
                // TODO: PENDING FROM HERE
            }


            else -> {}
        }
    }

    private fun cleanUpBgPlayers() {
        if (playersBackground.isNotEmpty()) {
            playersBackground.map { it.value.release() }
            playersBackground.clear()
        }
    }

    private fun synchronizedMainPlayers() {

        val mainListener = object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                playersBackground.map { if (isPlaying) it.value.play() else it.value.pause() }
            }

            override fun onPositionDiscontinuity(oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: Int) {
                // This event we are using to sync main and other player seek time line.
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)

                val durationMs = player.contentDuration
                if (durationMs > 0) {
                    val percentPosition: Float = (newPosition.positionMs.toFloat() / durationMs) //0.5 but got trim to 0
                    playersBackground.map {
                        it.value.apply {
                            val calPosition = (contentDuration * percentPosition).toLong()
                            seekTo(calPosition)
                        }
                    }
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {

                    Player.STATE_BUFFERING -> {
                        Log.i(TAG, "onIsPlayingChanged: STATE_BUFFERING")
                    }

                    Player.STATE_ENDED -> {

                    }

                    Player.STATE_IDLE -> {
                        Log.i(TAG, "onIsPlayingChanged:STATE_IDLE")
                    }

                    Player.STATE_READY -> {
                        Log.i(TAG, "onIsPlayingChanged: STATE_READY")
                    }
                }
            }

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                super.onTimelineChanged(timeline, reason)
                if (!timeline.isEmpty) {
                    val durationMs = timeline.getPeriod(0, Timeline.Period()).durationMs
                    Log.i(TAG, "onTimelineChanged: $durationMs")
                }
            }


            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                playersBackground.map { it.value.pause() }
            }
        }

        player.addListener(mainListener)
    }


    private fun synchronizedBGPlayers() {

        val bgListener = object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) player.play() else player.pause()
                Log.d("MSessionService", "onIsPlayingChanged:BG_EVENT $isPlaying ")
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                player.pause()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {

                    Player.STATE_BUFFERING -> {
                        Log.i("MSessionService", "onIsPlayingChanged:BG_EVENT STATE_BUFFERING")
                    }

                    Player.STATE_ENDED -> {}

                    Player.STATE_IDLE -> {
                        Log.i("MSessionService", "onIsPlayingChanged:BG_EVENT STATE_IDLE")
                    }

                    Player.STATE_READY -> {
                        Log.i("MSessionService", "onIsPlayingChanged:BG_EVENT STATE_READY")
                    }
                }
            }
        }

        playersBackground.forEach { it.value.addListener(bgListener) }
    }


}

