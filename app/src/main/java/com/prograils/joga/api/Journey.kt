package com.prograils.joga.api

data class Journey(
        val description: String,
        val id: String,
        val name: String
) {
    override fun toString(): String {
        return "$id, $description, $name"
    }
}