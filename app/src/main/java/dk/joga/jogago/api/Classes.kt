package dk.joga.jogago.api

import com.google.gson.annotations.SerializedName

data class Classes (
        val classes: List<Class>,
        val page: Int,
        @SerializedName("total_count")
        val totalCount: Int
        )