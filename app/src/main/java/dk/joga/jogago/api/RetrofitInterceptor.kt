package dk.joga.jogago.api

import android.content.Context
import dk.joga.jogago.R
import okhttp3.Interceptor
import okhttp3.Response

class RetrofitInterceptor(val context: Context) : Interceptor {
    private var tokenBase = "Bearer "

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val preferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE)

        if (originalRequest.method().equals("POST")) {
            return chain.proceed(originalRequest)
        }
        val tokenKey = preferences.getString(context.getString(R.string.saved_token_key), "")
        val newRequest = originalRequest.newBuilder().header("Authorization", tokenBase + tokenKey).build()

        return chain.proceed(newRequest)
    }
}