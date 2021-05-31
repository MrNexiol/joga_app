package dk.joga.jogago.api

import com.google.gson.annotations.SerializedName

data class Categories(
        val categories: List<Category>,
        val page: Int,
        @SerializedName("count")
        val totalCount: Int
)