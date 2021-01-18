package com.prograils.joga.ui.trainerDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.api.Instructor
import com.prograils.joga.api.Resource

class TrainerDetailViewModel(repository: Repository, trainerId: String, token: String) : ViewModel() {
    val instructor: LiveData<Resource<Instructor>> = repository.getInstructor(token, trainerId)
    var trainerClasses: LiveData<Resource<List<Class>>> = repository.getInstructorClasses(token, trainerId)
}