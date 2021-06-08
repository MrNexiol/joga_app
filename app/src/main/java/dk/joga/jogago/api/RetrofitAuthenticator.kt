package dk.joga.jogago.api

import android.content.Context
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class RetrofitAuthenticator(val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val updatedToken: String? = getUpdatedToken()

        return if (updatedToken == null) {
            null
        } else {
            response.request().newBuilder().header("Authorization", "Bearer $updatedToken").build()
        }
    }

    private fun getUpdatedToken(): String? {
        val preferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE)
        val oldToken = preferences.getString(context.getString(R.string.saved_token_key), null)
        val refreshToken = preferences.getString(context.getString(R.string.saved_refresh_token_key), null)
        var newToken: String? = null
        if (oldToken != null && refreshToken != null) {
            val reloginData = AppContainer.repository.refreshToken(oldToken, refreshToken)
            if (reloginData.status == Status.Success) {
                newToken = reloginData.data!!.token
                with(preferences.edit()) {
                    this.putString(context.getString(R.string.saved_token_key), newToken)
                    this.putString(context.getString(R.string.saved_refresh_token_key), reloginData.data.refreshToken)
                    this.commit()
                }
            }
        }
        return newToken
    }
}