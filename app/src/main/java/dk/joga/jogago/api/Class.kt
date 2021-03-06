package dk.joga.jogago.api

import com.google.gson.annotations.SerializedName

data class Class(
        val categories: List<Category>,
        val description: String,
        val duration: Int,
        val focus: String,
        val id: String,
        val instructor: Instructor,
        @SerializedName("thumbnail_url_resized")
        val thumbnailUrl: String,
        val title: String,
        @SerializedName("user_like")
        val userLike: UserLike,
        @SerializedName("vimeo_url")
        val videoUrl: String,
        val watched: Boolean,
        val watchable: Boolean,
        @SerializedName("release_date")
        val releaseDate: String,
        val new: Boolean
)