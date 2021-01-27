package com.prograils.joga.api

import com.google.gson.annotations.SerializedName

data class Class(
        val categories: List<Category>,
        val description: String,
        val duration: Int,
        val focus: String,
        val id: String,
        val instructor: Instructor,
        @SerializedName("thumbnail_url")
        val thumbnailUrl: String,
        val title: String,
        @SerializedName("user_like")
        val userLike: UserLike,
        @SerializedName("video_url")
        val videoUrl: String,
        val watched: Boolean
)