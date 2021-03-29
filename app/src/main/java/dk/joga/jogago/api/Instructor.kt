package dk.joga.jogago.api

import com.google.gson.annotations.SerializedName

data class Instructor(
        val id: String,
        val avatar_url: String,
        val name: String,
        @SerializedName("video_url")
        val videoUrl: String,
        @SerializedName("video_duration")
        val videoDuration: Int,
        @SerializedName("video_title")
        val videoTitle: String
)