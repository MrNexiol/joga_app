package com.prograils.joga.api

import com.google.gson.annotations.SerializedName

data class WelcomePopupResponse(
        @SerializedName("welcome_popup")
        val welcomePopup: WelcomePopup
)