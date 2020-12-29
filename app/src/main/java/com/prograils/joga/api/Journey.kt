package com.prograils.joga.api

import com.google.gson.annotations.SerializedName

data class Journey(
        val classes: List<Class>,
        @SerializedName("cover_url")
        val coverUrl: String,
        val description: String,
        val id: String,
        val name: String,
        @SerializedName("watched_classes_id")
        val watchedClassesIds: List<String>
) {
    override fun toString(): String {
        return "$id, $description, $name"
    }
}