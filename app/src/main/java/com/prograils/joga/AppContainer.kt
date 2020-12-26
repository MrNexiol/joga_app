package com.prograils.joga

import com.prograils.joga.api.WebService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {
    private val BASE_URL: String = "https://joga-go.prograils.net:443/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WebService::class.java)

    val repository = Repository(retrofit)
}