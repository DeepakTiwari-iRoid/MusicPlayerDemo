package com.app.musicplayerdemo.utils

import android.content.Context
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.app.musicplayerdemo.modal.MetaData
import com.app.musicplayerdemo.service.MusicPlayerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Constants {

    const val MEDIA_URIS = "MEDIA_URLS"
    const val PRIM_VOLUME = "PRIMARY_VOLUME"
    const val BG_SOUND = "BACKGROUND_SOUND_VOLUME"
    const val BG_SOUND_INDEX = "BACKGROUND_SOUND_INDEX"
    const val BG_SOUND_RANGE = "BACKGROUND_SOUND_RANGE"
    const val REPEAT_ALL = "REPEAT_MOOD"

    fun bgMusicTrackController(context: Context, itemKey: String, volume: Float) {
        val intent = Intent(context, MusicPlayerService::class.java)
        intent.setAction(BG_SOUND)
        intent.putExtra(BG_SOUND_INDEX, itemKey)
        intent.putExtra(BG_SOUND_RANGE, volume)
        context.startService(intent)
    }

    fun primeVolumeController(context: Context, volume: Float) {
        val intent = Intent(context, MusicPlayerService::class.java)
        intent.setAction(PRIM_VOLUME)
        intent.putExtra(PRIM_VOLUME, volume)
        context.startService(intent)
    }

    class Debouncer(private val debounceTime: Long = 100L) {
        private var job: Job? = null

        fun debounce(scope: CoroutineScope, call: () -> Unit) {
            job?.cancel() // Cancel any previous job
            job = scope.launch {
                delay(debounceTime)
                call() // Execute the call after the delay
            }
        }
    }


    private fun metaData(meta: MetaData): MediaMetadata {
        return MediaMetadata.Builder()
            .setArtist(meta.artist)
            .setTitle(meta.title)
            .setDescription(meta.description)
            .setDisplayTitle(meta.displayTitle)
            .setSubtitle(meta.subTitle)
            .build()
    }

    fun mediaItem(url: String, meta: MetaData): MediaItem {
        return MediaItem.Builder()
            .setUri(url)
            .setMediaMetadata(metaData(meta))
            .build()
    }

}