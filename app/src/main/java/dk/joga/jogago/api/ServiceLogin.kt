package dk.joga.jogago.api

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface ServiceLogin {
    @POST("sessions")
    fun login(@Query("username") username: String, @Query("password") password: String): Call<Login>

    @POST("sessions/extend")
    fun relogin(@Query("token") token: String, @Query("refresh_token") refreshToken: String): Call<Login>

    @DELETE("sessions")
    fun logout(): Call<Void>
}