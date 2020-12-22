package com.prograils.joga.api

data class Journey(
        private val description: String,
        private val id: String,
        private val name: String
) {
    override fun toString(): String {
        return "$id, $description, $name"
    }
}