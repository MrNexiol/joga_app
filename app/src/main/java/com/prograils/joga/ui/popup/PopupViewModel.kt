package com.prograils.joga.ui.popup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Resource
import com.prograils.joga.api.WelcomePopup

class PopupViewModel(repository: Repository, token: String) : ViewModel() {
    val welcomePopup: LiveData<Resource<WelcomePopup>> = repository.getWelcomePopup(token)
}