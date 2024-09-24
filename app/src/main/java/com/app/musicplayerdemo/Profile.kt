package com.app.musicplayerdemo

import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.app.musicplayerdemo.databinding.FragmentProfileBinding
import com.app.musicplayerdemo.service.MusicPlayerService
import com.app.musicplayerdemo.utils.Constants
import com.app.musicplayerdemo.utils.Constants.bgMusicTrackController
import com.app.musicplayerdemo.utils.Constants.primeVolumeController
import kotlinx.coroutines.Job

class Profile : Fragment() {

    companion object {
        const val TAG = "Profile Fragment"
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var job: Job? = null
    val deb = Constants.Debouncer()

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
            val itemKey = requireContext().getString(R.string.audio_sample_1)

            seekBarPrimary.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    seekBar?.let {
                        deb.debounce(lifecycleScope) {
                            Log.d(TAG, "onProgressChanged: PRIMARY DELAY $progress")
                            primeVolumeController(requireContext(), (seekBar.progress).div(100f))
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.i(TAG, "onStartTrackingTouch: ${seekBar?.progress}")
                    seekBarSecondary.progress = 0
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Log.i(TAG, "onStopTrackingTouch: ${seekBar?.progress}")
                    /*seekBar?.let {
                        primeVolumeController(requireContext(), (seekBar.progress).div(100f))
                    }*/
                }
            })


            seekBarSecondary.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Log.i(TAG, "onProgressChanged: seek $seekBar, progress $progress, fromUser $fromUser")
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.i(TAG, "onStartTrackingTouch: ${seekBar?.progress}")
                    seekBarPrimary.progress = 0
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        bgMusicTrackController(requireContext(), itemKey, seekBar.progress.div(100f))
                    }
                }
            })
        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


}


