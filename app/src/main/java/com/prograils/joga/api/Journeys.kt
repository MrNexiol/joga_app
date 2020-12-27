package com.prograils.joga.api

data class Journeys(
        val journeys: List<Journey>,
        val page: Int,
        val total_count: Int
)