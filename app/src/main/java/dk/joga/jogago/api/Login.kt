package dk.joga.jogago.api

import com.google.gson.annotations.SerializedName

data class Login(
        val token: String,
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("refresh_token")
        val refreshToken: String
)