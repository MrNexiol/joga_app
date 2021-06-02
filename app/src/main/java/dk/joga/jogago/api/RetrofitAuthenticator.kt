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
        var updatedToken: String?
        updatedToken = getUpdatedToken()
        if (updatedToken == null) {
            updatedToken = tryToLoginAgain()
        }
        
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
            val reloginData = AppContainer.repository.relogin(oldToken, refreshToken)
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

    private fun tryToLoginAgain(): String? {
        val preferences = context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE)
        val username = preferences.getString(context.getString(R.string.saved_username), null)
        val password = preferences.getString(context.getString(R.string.saved_password), null)
        var newToken: String? = null
        if (username != null && password != null) {
            val response = AppContainer.repository.syncLogin(username, password)
            if (response.status == Status.Success) {
                newToken = response.data!!.token
                with(preferences.edit()) {
                    this.putString(context.getString(R.string.saved_user_id), response.data.userId)
                    this.putString(context.getString(R.string.saved_token_key), response.data.token)
                    this.putString(context.getString(R.string.saved_refresh_token_key), response.data.refreshToken)
                    this.commit()
                }
            }
        }
        return newToken
    }
}