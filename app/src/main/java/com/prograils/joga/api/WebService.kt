package com.prograils.joga.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WebService {
    @GET("api/v1/instructors")
    fun getInstructors(): Call<Instructors>

    @GET("api/v1/journeys")
    fun getJourneys(): Call<Journeys>

    @GET("api/v1/journeys/{id}")
    fun getJourney(@Path("id") id: String): Call<JourneyResponse>

    @GET("api/v1/classes")
    fun getClasses(): Call<Classes>

    @GET("api/v1/daily_class")
    fun getDailyClass(): Call<ClassResponse>
}