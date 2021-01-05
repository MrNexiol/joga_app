package com.prograils.joga.api

import retrofit2.Call
import retrofit2.http.*

interface WebService {
    @GET("api/v1/instructors")
    fun getInstructors(): Call<Instructors>

    @GET("api/v1/instructors/{id}")
    fun getInstructor(@Path("id") id: String): Call<InstructorResponse>

    @GET("api/v1/journeys")
    fun getJourneys(): Call<Journeys>

    @GET("api/v1/journeys/{id}")
    fun getJourney(@Path("id") id: String): Call<JourneyResponse>

    @GET("api/v1/classes")
    fun getClasses(): Call<Classes>

    @GET("api/v1/liked_classes")
    fun getLikedClasses(@Header("Authorization") userToken: String): Call<Classes>

    @GET("api/v1/classes")
    fun getInstructorClasses(@Query("instructor_id") instructorId: String): Call<Classes>

    @GET("api/v1/daily_class")
    fun getDailyClass(): Call<ClassResponse>

    @GET("api/v1/classes/{id}")
    fun getClass(@Path("id") id: String, @Header("Authorization") userToken: String): Call<ClassResponse>

    @POST("api/v1/sessions")
    fun login(@Query("username") username: String, @Query("password") password: String): Call<Login>

    @POST("api/v1/like/{id}")
    fun toggleLike(@Path("id") id: String, @Header("Authorization") userToken: String): Call<Void>

    @DELETE("api/v1/sessions")
    fun logout(@Header("Authorization") userToken: String): Call<Void>
}