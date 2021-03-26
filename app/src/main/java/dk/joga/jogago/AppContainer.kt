package dk.joga.jogago

import android.content.Context
import dk.joga.jogago.api.RetrofitInterceptor
import dk.joga.jogago.api.WebService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {
    private val retrofitInterceptor: RetrofitInterceptor = RetrofitInterceptor(context)
    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder().addInterceptor(retrofitInterceptor).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(WebService::class.java)

    val repository = Repository(retrofit)
}