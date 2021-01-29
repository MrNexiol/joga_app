package com.prograils.joga

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prograils.joga.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val service: WebService) {
    fun getWelcomePopup(token: String): LiveData<Resource<WelcomePopup>>{
        val data = MutableLiveData<Resource<WelcomePopup>>()
        val auth = "Bearer $token"
        service.getWelcomePopup(auth).enqueue(object : Callback<WelcomePopupResponse>{
            override fun onResponse(call: Call<WelcomePopupResponse>, response: Response<WelcomePopupResponse>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.welcomePopup)
                    data.value = resource
                } else {
                    val resource = Resource(Status.Empty, null)
                    data.value = resource
                }
            }
            override fun onFailure(call: Call<WelcomePopupResponse>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getInstructors(token: String): LiveData<Resource<List<Instructor>>> {
        val data = MutableLiveData<Resource<List<Instructor>>>()
        val auth = "Bearer $token"
        service.getInstructors(auth).enqueue(object : Callback<Instructors>{
            override fun onResponse(call: Call<Instructors>, response: Response<Instructors>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.instructors)
                    data.value = resource
                }
            }
            override fun onFailure(call: Call<Instructors>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun getInstructor(token: String, id: String): LiveData<Resource<Instructor>> {
        val data = MutableLiveData<Resource<Instructor>>()
        val auth = "Bearer $token"
        service.getInstructor(auth, id).enqueue(object : Callback<InstructorResponse>{
            override fun onResponse(call: Call<InstructorResponse>, response: Response<InstructorResponse>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.instructor)
                    data.value = resource
                }
            }

            override fun onFailure(call: Call<InstructorResponse>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getJourneys(token: String): LiveData<Resource<List<Journey>>> {
        val data = MutableLiveData<Resource<List<Journey>>>()
        val auth = "Bearer $token"
        service.getJourneys(auth).enqueue(object : Callback<Journeys>{
            override fun onResponse(call: Call<Journeys>, response: Response<Journeys>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.journeys)
                    data.value = resource
                } else {
                    val resource = Resource(Status.Empty, null)
                    data.value = resource
                }
            }
            override fun onFailure(call: Call<Journeys>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun getJourney(token: String, id: String): LiveData<Resource<Journey>> {
        val data = MutableLiveData<Resource<Journey>>()
        val auth = "Bearer $token"
        service.getJourney(auth, id).enqueue(object : Callback<JourneyResponse>{
            override fun onResponse(call: Call<JourneyResponse>, response: Response<JourneyResponse>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.journey)
                    data.value = resource
                }
            }

            override fun onFailure(call: Call<JourneyResponse>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getClasses(token: String): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        val auth = "Bearer $token"
        service.getClasses(auth).enqueue(object : Callback<Classes>{
            override fun onResponse(call: Call<Classes>, response: Response<Classes>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.classes)
                    data.value = resource
                }
            }

            override fun onFailure(call: Call<Classes>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun getNewClasses(token: String): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        val auth = "Bearer $token"
        service.getNewClasses(auth).enqueue(object : Callback<Classes>{
            override fun onResponse(call: Call<Classes>, response: Response<Classes>) {
                if (response.body() != null){
                    if (response.body()!!.classes.isEmpty()){
                        val resource = Resource(Status.Empty, listOf<Class>())
                        data.value = resource
                    } else {
                        val resource = Resource(Status.Success, response.body()!!.classes)
                        data.value = resource
                    }
                }
            }

            override fun onFailure(call: Call<Classes>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getLikedClasses(token: String): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        val auth = "Bearer $token"
        service.getLikedClasses(auth).enqueue(object : Callback<Classes>{
            override fun onResponse(call: Call<Classes>, response: Response<Classes>) {
                if (response.body() != null){
                    if (response.body()!!.classes.isEmpty()){
                        val resource = Resource(Status.Empty, listOf<Class>())
                        data.value = resource
                    } else {
                        val resource = Resource(Status.Success, response.body()!!.classes)
                        data.value = resource
                    }
                }
            }

            override fun onFailure(call: Call<Classes>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getInstructorClasses(token: String, instructorId: String): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        val auth = "Bearer $token"
        service.getInstructorClasses(auth, instructorId).enqueue(object : Callback<Classes>{
            override fun onResponse(call: Call<Classes>, response: Response<Classes>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.classes)
                    data.value = resource
                }
            }

            override fun onFailure(call: Call<Classes>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getDailyClass(token: String): LiveData<Resource<Class>> {
        val data = MutableLiveData<Resource<Class>>()
        val auth = "Bearer $token"
        service.getDailyClass(auth).enqueue(object : Callback<ClassResponse>{
            override fun onResponse(call: Call<ClassResponse>, response: Response<ClassResponse>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.lecture)
                    data.value = resource
                } else {
                    val resource = Resource(Status.Fail, null)
                    data.value = resource
                }
            }

            override fun onFailure(call: Call<ClassResponse>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun getClass(token: String, id: String): LiveData<Resource<Class>> {
        val data = MutableLiveData<Resource<Class>>()
        val auth = "Bearer $token"
        service.getClass(auth, id).enqueue(object : Callback<ClassResponse>{
            override fun onResponse(call: Call<ClassResponse>, response: Response<ClassResponse>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.lecture)
                    data.value = resource
                } else {
                    val resource = Resource(Status.Fail, null)
                    data.value = resource
                }
            }

            override fun onFailure(call: Call<ClassResponse>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun login(username: String, password: String): LiveData<Resource<Login>>{
        val data = MutableLiveData<Resource<Login>>()
        service.login(username, password).enqueue(object : Callback<Login>{
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body())
                    data.value = resource
                } else {
                    val resource = Resource(Status.Fail, response.body())
                    data.value = resource
                }
            }
            override fun onFailure(call: Call<Login>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun toggleClassLike(token: String, id: String): LiveData<Resource<Void>>{
        val data = MutableLiveData<Resource<Void>>()
        val auth = "Bearer $token"
        service.toggleLike(auth, id).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200){
                    val resource = Resource(Status.Success, response.body())
                    data.value = resource
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun markClassAsWatched(token: String, classId: String): LiveData<Resource<Void>>{
        val data = MutableLiveData<Resource<Void>>()
        val auth = "Bearer $token"
        service.markClassAsWatched(auth, classId).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 201){
                    val resource = Resource(Status.Success, response.body())
                    data.value = resource
                } else {
                    val resource = Resource(Status.Fail, response.body())
                    data.value = resource
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                val resource = Resource(Status.Success, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun logout(token: String): LiveData<Resource<Void>>{
        val data = MutableLiveData<Resource<Void>>()
        val auth = "Bearer $token"
        service.logout(auth).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200){
                    val resource = Resource(Status.Success, response.body())
                    data.value = resource
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }
}