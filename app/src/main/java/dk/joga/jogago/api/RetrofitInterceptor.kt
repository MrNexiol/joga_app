package dk.joga.jogago.api

import android.content.Context
import dk.joga.jogago.R
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RetrofitInterceptor(val context: Context) : Interceptor {
    private var TOKEN_BASE: String = "Bearer"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val preferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE)

        if (originalRequest.url().toString().contains("sessions") && originalRequest.method().equals("POST")) {
            return chain.proceed(originalRequest)
        }
        val tokenKey = preferences.getString(context.getString(R.string.saved_token_key), "")
        val newRequest: Request = originalRequest.newBuilder().header("Authorization", "$TOKEN_BASE $tokenKey").build()

        return chain.proceed(newRequest)
    }
}