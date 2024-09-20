package com.app.musicplayerdemo.service


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

const val TAG = "MSessionService"

class MusicPlayerService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer

    //    private lateinit var playerNarrators: ExoPlayer
    private val playersBackground: MutableMap<String, ExoPlayer> = mutableMapOf()

    override fun onCreate() {
        super.onCreate()

        player = createPlayer(this, true)
//        playerNarrators = createPlayer(this)

        // Configure media session for video playback as well
        mediaSession = MediaSession.Builder(this, player).build()
        synchronizedMainPlayers()
    }


    private fun createPlayer(context: Context, handleAutoFocus: Boolean = false): ExoPlayer {

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        return ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, handleAutoFocus)
            .setHandleAudioBecomingNoisy(true)
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .build()
    }

    private fun addBackGroundMusics(mediaUrls: List<String>) {
        mediaUrls.map {
            val player = createPlayer(this)  // Create a new player
            val mediaItem = MediaItem.fromUri(it)  // Create a media item from URI
            player.setMediaItem(mediaItem)  // Add media item to player
            player.prepare()  // Prepare the player
            playersBackground[it] = player  // Store the player in the map
        }
        Log.i(TAG, "addBackGroundMusics: ${playersBackground.keys}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val mediaUrls = it.getStringArrayListExtra("MEDIA_URLS")
            mediaUrls?.let { urls ->
                cleanUpBgPlayers()
                addBackGroundMusics(urls)
                synchronizedBGPlayers()
            }
        }
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

    private fun cleanUpBgPlayers() {
        playersBackground.map { it.value.release() }
        playersBackground.clear()
    }

    private fun synchronizedMainPlayers() {

        val mainListener = object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                playersBackground.map { if (isPlaying) it.value.play() else it.value.pause() }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                playersBackground.map { it.value.pause() }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {

                    Player.STATE_BUFFERING -> {

                    }

                    Player.STATE_ENDED -> {

                    }

                    Player.STATE_IDLE -> {

                    }

                    Player.STATE_READY -> {

                    }
                }
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

