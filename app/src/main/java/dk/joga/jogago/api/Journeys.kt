package dk.joga.jogago.api

import com.google.gson.annotations.SerializedName

data class Journeys(
        val journeys: List<Journey>,
        val page: Int,
        @SerializedName("total_count")
        val totalCount: Int
)