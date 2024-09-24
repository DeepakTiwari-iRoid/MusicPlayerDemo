package com.app.musicplayerdemo

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.musicplayerdemo.adapters.MusicAdapter
import com.app.musicplayerdemo.databinding.FragmentHomeBinding
import com.app.musicplayerdemo.modal.MetaData
import com.app.musicplayerdemo.modal.Songs
import com.app.musicplayerdemo.service.MusicPlayerService
import com.app.musicplayerdemo.utils.Constants.MEDIA_URIS
import com.app.musicplayerdemo.utils.Constants.mediaItem
import com.google.common.util.concurrent.MoreExecutors

class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sessionToken by lazy { SessionToken(requireContext(), ComponentName(requireContext(), MusicPlayerService::class.java)) }
    private val mediaController by lazy { MediaController.Builder(requireContext(), sessionToken).buildAsync() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        setup()
        return binding.root
    }

    @OptIn(UnstableApi::class)
    private fun setup() {
        with(binding) {


            val intent = Intent(requireContext(), MusicPlayerService::class.java)
            intent.setAction(MEDIA_URIS)

            intent.putStringArrayListExtra(
                MEDIA_URIS, arrayListOf(
                    "https://dev.iroidsolutions.com/kavana-meditation-backend/public/storage/content/background_music_file/z187m05zoNUhypGOeldqF6Jan33hK3wGCIgQCCdb.mp3",
                    getString(R.string.audio_sample_1),
                )
            )

            rvMainMusic.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvMainMusic.adapter = MusicAdapter(this@Home.requireContext(), songs()) { song ->

                val uid = System.currentTimeMillis()

                mediaController.addListener(
                    {
                        val mediaController = if (mediaController.isDone) mediaController.get() else null
                        mediaController?.setMediaItems(listOf(mediaItem(song.url, MetaData(title = "$uid")), mediaItem(song.url, MetaData(title = "2+$uid+2"))))
                        mediaController?.prepare()
                        mediaController?.play()
                        requireContext().startService(intent)
                    },
                    MoreExecutors.directExecutor()
                )
            }
        }
    }


    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onPause: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: ")
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    private fun songs(): ArrayList<Songs> {
        return arrayListOf(
            Songs(title = "Test Title", author = "Test Author", url = getString(R.string.media_url_mp4)),
            Songs(title = "Test Title1", author = "Test Author1", url = getString(R.string.media_url_mp4)),
            Songs(title = "Test Title2", author = "Test Author2", url = getString(R.string.media_url_mp4)),
            Songs(title = "Test Title3", author = "Test Author3", url = getString(R.string.media_url_mp4)),
        )
    }

}