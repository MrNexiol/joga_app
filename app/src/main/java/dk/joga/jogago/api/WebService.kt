package dk.joga.jogago.api

import retrofit2.Call
import retrofit2.http.*

interface WebService {
    @GET("welcome_popup")
    fun getWelcomePopup(): Call<WelcomePopupResponse>

    @GET("instructors")
    fun getInstructors(): Call<Instructors>

    @GET("instructors/{id}")
    fun getInstructor(@Path("id") id: String): Call<InstructorResponse>

    @GET("journeys")
    fun getJourneys(@Query("page") page: Int = 1): Call<Journeys>

    @GET("journeys/{id}")
    fun getJourney(@Path("id") id: String): Call<JourneyResponse>

    @GET("categories")
    fun getCategories(@Query("page") page: Int = 1): Call<Categories>

    @GET("classes")
    fun getClasses(@Query("category_id") categoryId: String, @Query("page") page: Int = 1): Call<Classes>

    @GET("interesting_classes")
    fun getNewClasses(): Call<Classes>

    @GET("liked_classes")
    fun getLikedClasses(@Query("page") page: Int = 1): Call<Classes>

    @GET("classes")
    fun getInstructorClasses(@Query("instructor_id") instructorId: String, @Query("page") page: Int = 1): Call<Classes>

    @GET("daily_class")
    fun getDailyClass(): Call<ClassResponse>

    @GET("classes/{id}")
    fun getClass(@Path("id") id: String): Call<ClassResponse>

    @POST("sessions")
    fun login(@Query("username") username: String, @Query("password") password: String): Call<Login>

    @POST("sessions/extend")
    fun relogin(@Query("token") token: String, @Query("refresh_token") refreshToken: String): Call<Login>

    @POST("like/{id}")
    fun toggleLike(@Path("id") id: String): Call<Void>

    @POST("classes/{id}/mark_as_watched")
    fun markClassAsWatched(@Path("id") id: String): Call<Void>

    @DELETE("sessions")
    fun logout(): Call<Void>
}