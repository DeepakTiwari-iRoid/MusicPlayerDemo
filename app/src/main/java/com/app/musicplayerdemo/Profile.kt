package com.app.musicplayerdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.app.musicplayerdemo.databinding.FragmentProfileBinding
import com.app.musicplayerdemo.service.MusicPlayerService
import com.app.musicplayerdemo.utils.Constants.BG_MUSIC_VOL
import com.app.musicplayerdemo.utils.Constants.MUTE_NON_PRIME_BG_MUSIC
import com.app.musicplayerdemo.utils.Constants.PRIM_VOLUME

class Profile : Fragment() {

    companion object {
        const val TAG = "Profile Fragment"
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private val sessionToken by lazy { SessionToken(requireContext(), ComponentName(requireContext(), MusicPlayerService::class.java)) }
    private val mediaController by lazy { MediaController.Builder(requireContext(), sessionToken).buildAsync() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        setup()
        return binding.root
    }

    private fun setup() {
        with(binding) {

            seekBarPrimary.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Log.i(TAG, "onProgressChanged: seek $seekBar, progress $progress, fromUser $fromUser")

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.i(TAG, "onStartTrackingTouch: ${seekBar?.progress}")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        primeVolumeController(requireContext(), (seekBar.progress).div(100f))
                    }
                }
            })

            seekBarSecondary.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Log.i(TAG, "onProgressChanged: seek $seekBar, progress $progress, fromUser $fromUser")

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.i(TAG, "onStartTrackingTouch: ${seekBar?.progress}")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        primeVolumeController(requireContext(), (seekBar.progress).div(100f))
                    }
                }
            })


        }
    }

    fun primeVolumeController(context: Context, volume: Float) {
        val intent = Intent(context, MusicPlayerService::class.java)
        intent.setAction(PRIM_VOLUME)
        intent.putExtra(PRIM_VOLUME, volume)
        context.startService(intent)
    }

    fun bgMusicTrackController(context: Context, muteItems: ArrayList<String>) {
        val intent = Intent(context, MusicPlayerService::class.java)
        intent.setAction(BG_MUSIC_VOL)
        intent.putExtra(MUTE_NON_PRIME_BG_MUSIC, muteItems)
        context.startService(intent)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


}