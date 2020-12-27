package com.prograils.joga.ui.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Classes

class ClassesViewModel(repository: Repository) : ViewModel() {
    val classes: LiveData<Classes> = repository.getClasses()
}