package dk.joga.jogago

import dk.joga.jogago.api.WebService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WebService::class.java)

    val repository = Repository(retrofit)
}