package dk.joga.jogago.api

import com.google.gson.annotations.SerializedName

data class ClassResponse(
        @SerializedName("class")
        val lecture: Class
)