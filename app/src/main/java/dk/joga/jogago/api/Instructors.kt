package dk.joga.jogago.api

import com.google.gson.annotations.SerializedName

data class Instructors(
    val instructors: List<Instructor>,
    val page: Int,
    @SerializedName("count")
    val totalCount: Int
    )