package com.prograils.joga.api

data class Journeys(
        val journeys: List<Journey>,
        private val page: Int,
        private val total_count: Int
) {
}