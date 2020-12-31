package com.prograils.joga

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prograils.joga.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(
    private val service: WebService
) {
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

    fun getInstructors(): LiveData<Resource<List<Instructor>>> {
        val data = MutableLiveData<Resource<List<Instructor>>>()
        service.getInstructors().enqueue(object : Callback<Instructors>{
            override fun onResponse(call: Call<Instructors>, response: Response<Instructors>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.instructors)
                    data.value = resource
                } else {
                    TODO("Error handle")
                }
            }
            override fun onFailure(call: Call<Instructors>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun getJourneys(): LiveData<Resource<List<Journey>>> {
        val data = MutableLiveData<Resource<List<Journey>>>()
        service.getJourneys().enqueue(object : Callback<Journeys>{
            override fun onResponse(call: Call<Journeys>, response: Response<Journeys>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.journeys)
                    data.value = resource
                } else {
                    TODO("Error handle")
                }
            }
            override fun onFailure(call: Call<Journeys>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun getJourney(id: String): LiveData<Resource<Journey>> {
        val data = MutableLiveData<Resource<Journey>>()
        service.getJourney(id).enqueue(object : Callback<JourneyResponse>{
            override fun onResponse(call: Call<JourneyResponse>, response: Response<JourneyResponse>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.journey)
                    data.value = resource
                } else {
                    TODO("Error handle")
                }
            }

            override fun onFailure(call: Call<JourneyResponse>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getClasses(): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        service.getClasses().enqueue(object : Callback<Classes>{
            override fun onResponse(call: Call<Classes>, response: Response<Classes>) {
                if (response.body() != null){
                    val resource = Resource(Status.Success, response.body()!!.classes)
                    data.value = resource
                } else {
                    TODO("Error handle")
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
                    val resource = Resource(Status.Success, response.body()!!.classes)
                    data.value = resource
                } else {
                    TODO("Error handle")
                }
            }

            override fun onFailure(call: Call<Classes>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getDailyClass(): LiveData<Resource<Class>> {
        val data = MutableLiveData<Resource<Class>>()
        service.getDailyClass().enqueue(object : Callback<ClassResponse>{
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
}