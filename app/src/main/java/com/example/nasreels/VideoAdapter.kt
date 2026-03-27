package com.example.nasreels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView

class VideoAdapter(
    private val urls: List<String>
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var activeHolder: VideoViewHolder? = null

    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerView: PlayerView = view.findViewById(R.id.playerView)
        var player: ExoPlayer? = null

        fun bind(url: String) {
            releasePlayer()

            val context = itemView.context
            val exoPlayer = ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(url))
                repeatMode = Player.REPEAT_MODE_ONE
                playWhenReady = false
                prepare()
            }

            player = exoPlayer
            playerView.player = exoPlayer
        }

        fun play() {
            player?.playWhenReady = true
            player?.play()
        }

        fun pause() {
            player?.playWhenReady = false
            player?.pause()
        }

        fun releasePlayer() {
            playerView.player = null
            player?.release()
            player = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(urls[position])
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        if (activeHolder == holder) {
            activeHolder = null
        }
        holder.releasePlayer()
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int = urls.size

    fun playAtPosition(recyclerView: RecyclerView, position: Int) {
        if (position !in urls.indices) return

        val nextHolder = recyclerView.findViewHolderForAdapterPosition(position) as? VideoViewHolder
            ?: return

        if (activeHolder != nextHolder) {
            activeHolder?.pause()
            activeHolder = nextHolder
        }

        nextHolder.play()
    }

    fun pauseActive() {
        activeHolder?.pause()
    }

    fun releaseAll(recyclerView: RecyclerView) {
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val holder = recyclerView.getChildViewHolder(child) as? VideoViewHolder
            holder?.releasePlayer()
        }
        activeHolder = null
    }
}
