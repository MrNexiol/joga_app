package com.prograils.joga.api

data class Instructor(
    private val id: String,
    private val avatar_url: String,
    private val name: String
) {
    override fun toString(): String {
        return "$id, $avatar_url, $name"
    }
}