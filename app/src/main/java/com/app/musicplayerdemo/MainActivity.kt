package com.app.musicplayerdemo

import android.content.ComponentName
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.musicplayerdemo.databinding.ActivityMainBinding
import com.app.musicplayerdemo.service.MusicPlayerService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

//ZEROONEZERO
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var sessionToken: SessionToken? = null
    private var factory: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        setup()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()

        sessionToken = SessionToken(this, ComponentName(this, MusicPlayerService::class.java))
        factory = MediaController.Builder(this, sessionToken!!).buildAsync()

        factory?.addListener(
            {
                mediaController = factory?.let { if (it.isDone) it.get() else null }
                binding.miniPlayer.player = mediaController
                mediaController?.let { setupMiniPlayerControls(it) }
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun setupMiniPlayerControls(mediaController: MediaController) {

        mediaController.mediaMetadata.apply {
            binding.let { miniplayer ->
                miniplayer.miniMusicTitle.text = title
                miniplayer.miniMusicAuthor.text = artist
            }
        }

        mediaController.hasNextMediaItem()


        // Play/Pause button
        binding.miniPlay.setOnClickListener {
            if (mediaController.isPlaying) {
                mediaController.pause()
            } else {
                mediaController.play()
            }
        }

        // Previous button
        binding.miniPrevious.setOnClickListener {
            mediaController.seekToPreviousMediaItem()
        }

        // Next button
        binding.miniNext.setOnClickListener {
            mediaController.seekToNextMediaItem()
        }

        // Update the mini-player UI based on playback state
        mediaController.addListener(listener)
    }

    private fun setup() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private val listener = object : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {

                Player.STATE_BUFFERING -> {
                    with(binding) {
                        miniLoading.visibility = View.VISIBLE
                        miniPlay.visibility = View.GONE
                    }
                }

                Player.STATE_ENDED -> {
                }

                Player.STATE_IDLE -> {

                }

                Player.STATE_READY -> {
                    with(binding) {
                        miniLoading.visibility = View.GONE
                        miniPlay.visibility = View.VISIBLE
                    }
                }
            }
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            super.onMediaMetadataChanged(mediaMetadata)
            binding.miniMusicTitle.text = mediaMetadata.title ?: "Unknown"
            binding.miniMusicAuthor.text = mediaMetadata.artist ?: "Unknown"
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            // Update play/pause button state
            binding.miniPlay.setImageResource(
                if (isPlaying) R.drawable.baseline_pause_24 else R.drawable.play
            )
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            // Update UI with the new media item details (e.g., title/author)
            binding.miniMusicTitle.text = mediaItem?.mediaMetadata?.title ?: "Unknown"
            binding.miniMusicTitle.text = mediaItem?.mediaMetadata?.artist ?: "Unknown"
        }
    }

    override fun onStop() {
        super.onStop()
        factory.let {
            mediaController?.release()
            mediaController = null
        }
        factory = null
    }
}

//https://firebasestorage.googleapis.com/v0/b/anti-corruption-2104.appspot.com/o/audios%2F-NXJmyWz4O2com2tbzRT?alt=media&token=691a828a-64eb-4773-90ab-433ecaecbaa0
//https://medium.com/@debz_exe/implementation-of-media-3-mastering-background-playback-with-mediasessionservice-and-5e130272c39e

