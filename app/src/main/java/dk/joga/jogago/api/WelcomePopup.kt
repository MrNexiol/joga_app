package dk.joga.jogago.api

import com.google.gson.annotations.SerializedName

data class WelcomePopup(
        @SerializedName("inserted_at")
        val insertedAt: String,
        @SerializedName("text_da")
        val textDa: String,
        @SerializedName("text_en")
        val textEn: String,
        @SerializedName("title_da")
        val titleDa: String,
        @SerializedName("title_en")
        val titleEn: String
)