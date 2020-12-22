package com.prograils.joga.api

import retrofit2.Call
import retrofit2.http.GET

interface WebService {
    @GET("/api/v1/instructors")
    fun getInstructors(): Call<Instructors>

    @GET("/api/v1/journeys")
    fun getJourneys(): Call<Journeys>
}