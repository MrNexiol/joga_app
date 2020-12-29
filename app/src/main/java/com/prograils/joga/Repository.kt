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
                val resource = Resource(Status.Success, null, t)
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
                val resource = Resource(Status.Success, null, t)
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
                val resource = Resource(Status.Success, null, t)
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
                val resource = Resource(Status.Success, null, t)
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
                    TODO("Error handle")
                }
            }

            override fun onFailure(call: Call<ClassResponse>, t: Throwable) {
                val resource = Resource(Status.Success, null, t)
                data.value = resource
            }
        })
        return data
    }
}