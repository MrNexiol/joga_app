package com.prograils.joga.api

import retrofit2.Call
import retrofit2.http.*

interface WebService {
    @GET("api/v1/instructors")
    fun getInstructors(@Header("Authorization") userToken: String): Call<Instructors>

    @GET("api/v1/instructors/{id}")
    fun getInstructor(@Header("Authorization") userToken: String, @Path("id") id: String): Call<InstructorResponse>

    @GET("api/v1/journeys")
    fun getJourneys(@Header("Authorization") userToken: String): Call<Journeys>

    @GET("api/v1/journeys/{id}")
    fun getJourney(@Header("Authorization") userToken: String, @Path("id") id: String): Call<JourneyResponse>

    @GET("api/v1/classes")
    fun getClasses(@Header("Authorization") userToken: String): Call<Classes>

    @GET("api/v1/interesting_classes")
    fun getNewClasses(@Header("Authorization") userToken: String): Call<Classes>

    @GET("api/v1/liked_classes")
    fun getLikedClasses(@Header("Authorization") userToken: String): Call<Classes>

    @GET("api/v1/classes")
    fun getInstructorClasses(@Header("Authorization") userToken: String, @Query("instructor_id") instructorId: String): Call<Classes>

    @GET("api/v1/daily_class")
    fun getDailyClass(@Header("Authorization") userToken: String): Call<ClassResponse>

    @GET("api/v1/classes/{id}")
    fun getClass(@Header("Authorization") userToken: String, @Path("id") id: String): Call<ClassResponse>

    @POST("api/v1/sessions")
    fun login(@Query("username") username: String, @Query("password") password: String): Call<Login>

    @POST("api/v1/like/{id}")
    fun toggleLike(@Header("Authorization") userToken: String, @Path("id") id: String): Call<Void>

    @DELETE("api/v1/sessions")
    fun logout(@Header("Authorization") userToken: String): Call<Void>
}