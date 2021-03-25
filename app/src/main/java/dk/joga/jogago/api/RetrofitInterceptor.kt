package dk.joga.jogago.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RetrofitInterceptor : Interceptor {
    private var token: String = "Bearer "

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        if ("session".contains(originalRequest.url().toString())) {
            return chain.proceed(originalRequest)
        }
        val newRequest: Request = originalRequest.newBuilder().header("Authorization", token).build()

        return chain.proceed(newRequest)
    }

    fun changeToken(newToken: String) {
        token += newToken
    }
}