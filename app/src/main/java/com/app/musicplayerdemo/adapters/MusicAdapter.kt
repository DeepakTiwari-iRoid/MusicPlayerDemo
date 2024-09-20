package com.app.musicplayerdemo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.musicplayerdemo.databinding.MusicItemBinding
import com.app.musicplayerdemo.modal.Songs

class MusicAdapter(private val context: Context, private val songs: ArrayList<Songs>, val onItemClick: (Songs) -> Unit) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    inner class MusicViewHolder(val binding: MusicItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val layout = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val song = songs[position]

        with(holder.binding) {
            musicTitle.text = song.title
            musicAuthor.text = song.author
        }

        holder.itemView.setOnClickListener {
            onItemClick(song)
        }
    }

}