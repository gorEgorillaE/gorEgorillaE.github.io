package com.example.nasreels

import android.content.Context

private const val PREFS_NAME = "nas_reels_prefs"
private const val KEY_SERVER_IP = "server_ip"
private const val KEY_VIDEO_COUNT = "video_count"

object AppSettings {
    private const val DEFAULT_IP = "192.168.67.2"
    private const val DEFAULT_VIDEO_COUNT = 20

    fun getServerIp(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_SERVER_IP, DEFAULT_IP)
            ?: DEFAULT_IP
    }

    fun setServerIp(context: Context, ip: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SERVER_IP, ip)
            .apply()
    }

    fun getVideoCount(context: Context): Int {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_VIDEO_COUNT, DEFAULT_VIDEO_COUNT)
            .coerceAtLeast(1)
    }

    fun setVideoCount(context: Context, count: Int) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_VIDEO_COUNT, count.coerceAtLeast(1))
            .apply()
    }

    fun buildVideoUrls(context: Context): List<String> {
        val ip = getServerIp(context)
        val count = getVideoCount(context)

        return (1..count).map { index ->
            "http://$ip/videos/$index.mp4"
        }
    }
}
