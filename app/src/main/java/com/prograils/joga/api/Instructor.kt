package com.prograils.joga.api

data class Instructor(
        val id: String,
        val avatar_url: String,
        val name: String
) {
    override fun toString(): String {
        return "$id, $avatar_url, $name"
    }
}