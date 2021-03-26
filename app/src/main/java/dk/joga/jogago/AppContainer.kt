package dk.joga.jogago

import android.content.Context
import dk.joga.jogago.api.RetrofitInterceptor
import dk.joga.jogago.api.WebService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppContainer {
    lateinit var repository: Repository

    fun init(context: Context) {
        val retrofitInterceptor = RetrofitInterceptor(context)
        val okHttpClient = OkHttpClient().newBuilder().addInterceptor(retrofitInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WebService::class.java)
        repository = Repository(retrofit)
    }
}