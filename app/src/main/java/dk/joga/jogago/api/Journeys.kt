package dk.joga.jogago.api

data class Journeys(
        val journeys: List<Journey>,
        val page: Int,
        val total_count: Int
)