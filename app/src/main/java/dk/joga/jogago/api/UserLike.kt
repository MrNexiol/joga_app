package dk.joga.jogago.api

import com.google.gson.annotations.SerializedName

data class UserLike(
        @SerializedName("class_id")
        val classId: String,
        val id: String,
        @SerializedName("user_id")
        val userId: String
)