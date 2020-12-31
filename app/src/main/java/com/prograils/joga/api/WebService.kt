package com.prograils.joga.api

import retrofit2.Call
import retrofit2.http.*

interface WebService {
    @POST("api/v1/sessions")
    fun login(@Query("username") username: String, @Query("password") password: String): Call<Login>

    @DELETE("api/v1/sessions")
    fun logout(@Header("Authorization") userToken: String): Call<Void>

    @GET("api/v1/instructors")
    fun getInstructors(): Call<Instructors>

    @GET("api/v1/journeys")
    fun getJourneys(): Call<Journeys>

    @GET("api/v1/journeys/{id}")
    fun getJourney(@Path("id") id: String): Call<JourneyResponse>

    @GET("api/v1/classes")
    fun getClasses(): Call<Classes>

    @GET("api/v1/liked_classes")
    fun getLikedClasses(@Header("Authorization") userToken: String): Call<Classes>

    @GET("api/v1/daily_class")
    fun getDailyClass(): Call<ClassResponse>
}