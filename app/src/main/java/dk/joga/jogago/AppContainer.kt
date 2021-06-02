package dk.joga.jogago

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dk.joga.jogago.api.RetrofitAuthenticator
import dk.joga.jogago.api.RetrofitInterceptor
import dk.joga.jogago.api.WebService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppContainer {
    lateinit var repository: Repository
    lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init(context: Context) {
        val retrofitInterceptor = RetrofitInterceptor(context)
        val retrofitAuthenticator = RetrofitAuthenticator(context)
        val okHttpClient = OkHttpClient().newBuilder().addInterceptor(retrofitInterceptor).authenticator(retrofitAuthenticator).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WebService::class.java)
        repository = Repository(retrofit)
        firebaseAnalytics = Firebase.analytics
    }
}