package com.example.nasreels

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private val snapHelper = PagerSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        videoAdapter = VideoAdapter(AppSettings.buildVideoUrls(this))
        recyclerView.adapter = videoAdapter

        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    autoplaySnappedVideo()
                }
            }
        })

        recyclerView.post { autoplaySnappedVideo() }
    }

    override fun onResume() {
        super.onResume()
        val newUrls = AppSettings.buildVideoUrls(this)
        if (videoAdapter.itemCount != newUrls.size) {
            videoAdapter = VideoAdapter(newUrls)
            recyclerView.adapter = videoAdapter
            autoplaySnappedVideo()
        } else {
            autoplaySnappedVideo()
        }
    }

    override fun onPause() {
        videoAdapter.pauseActive()
        super.onPause()
    }

    override fun onDestroy() {
        videoAdapter.releaseAll(recyclerView)
        super.onDestroy()
    }

    private fun autoplaySnappedVideo() {
        val layoutManager = recyclerView.layoutManager ?: return
        val snappedView = snapHelper.findSnapView(layoutManager) ?: return
        val position = recyclerView.getChildAdapterPosition(snappedView)
        videoAdapter.playAtPosition(recyclerView, position)
    }
}
