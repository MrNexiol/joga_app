package dk.joga.jogago.api

import retrofit2.Call
import retrofit2.http.*

interface WebService {
    @GET("welcome_popup")
    fun getWelcomePopup(@Header("Authorization") userToken: String): Call<WelcomePopupResponse>

    @GET("instructors")
    fun getInstructors(@Header("Authorization") userToken: String): Call<Instructors>

    @GET("instructors/{id}")
    fun getInstructor(@Header("Authorization") userToken: String, @Path("id") id: String): Call<InstructorResponse>

    @GET("journeys")
    fun getJourneys(@Header("Authorization") userToken: String): Call<Journeys>

    @GET("journeys/{id}")
    fun getJourney(@Header("Authorization") userToken: String, @Path("id") id: String): Call<JourneyResponse>

    @GET("categories")
    fun getCategories(@Header("Authorization") userToken: String): Call<Categories>

    @GET("classes")
    fun getClasses(@Header("Authorization") userToken: String, @Query("category_id") categoryId: String): Call<Classes>

    @GET("interesting_classes")
    fun getNewClasses(@Header("Authorization") userToken: String): Call<Classes>

    @GET("liked_classes")
    fun getLikedClasses(@Header("Authorization") userToken: String): Call<Classes>

    @GET("classes")
    fun getInstructorClasses(@Header("Authorization") userToken: String, @Query("instructor_id") instructorId: String): Call<Classes>

    @GET("daily_class")
    fun getDailyClass(@Header("Authorization") userToken: String): Call<ClassResponse>

    @GET("classes/{id}")
    fun getClass(@Header("Authorization") userToken: String, @Path("id") id: String): Call<ClassResponse>

    @POST("sessions")
    fun login(@Query("username") username: String, @Query("password") password: String): Call<Login>

    @POST("like/{id}")
    fun toggleLike(@Header("Authorization") userToken: String, @Path("id") id: String): Call<Void>

    @POST("classes/{id}/mark_as_watched")
    fun markClassAsWatched(@Header("Authorization") userToken: String, @Path("id") id: String): Call<Void>

    @DELETE("sessions")
    fun logout(@Header("Authorization") userToken: String): Call<Void>
}