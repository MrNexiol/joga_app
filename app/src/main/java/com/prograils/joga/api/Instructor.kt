package com.prograils.joga.api

import com.google.gson.annotations.SerializedName

data class Instructor(
        val id: String,
        val avatar_url: String,
        val name: String,
        @SerializedName("video_url")
        val videoUrl: String
)