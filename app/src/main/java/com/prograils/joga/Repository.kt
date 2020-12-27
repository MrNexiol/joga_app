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
    fun getInstructors(): LiveData<Instructors> {
        val data = MutableLiveData<Instructors>()
        service.getInstructors().enqueue(object : Callback<Instructors>{
            override fun onResponse(call: Call<Instructors>, response: Response<Instructors>) {
                if (response.body() != null){
                    data.value = response.body()
                } else {
                    TODO("Error handle")
                }
            }
            override fun onFailure(call: Call<Instructors>, t: Throwable) {
                TODO("Error handle")
            }
        })
        return data
    }

    fun getJourneys(): LiveData<Journeys> {
        val data = MutableLiveData<Journeys>()
        service.getJourneys().enqueue(object : Callback<Journeys>{
            override fun onResponse(call: Call<Journeys>, response: Response<Journeys>) {
                if (response.body() != null){
                    data.value = response.body()
                } else {
                    TODO("Error handle")
                }
            }

            override fun onFailure(call: Call<Journeys>, t: Throwable) {
                TODO("Error handle")
            }
        })
        return data
    }

    fun getClasses(): LiveData<Classes> {
        val data = MutableLiveData<Classes>()
        service.getClasses().enqueue(object : Callback<Classes>{
            override fun onResponse(call: Call<Classes>, response: Response<Classes>) {
                if (response.body() != null){
                    data.value = response.body()
                } else {
                    TODO("Error handle")
                }
            }

            override fun onFailure(call: Call<Classes>, t: Throwable) {
                TODO("Error handle")
            }
        })
        return data
    }

    fun getDailyClass(): LiveData<ClassResponse> {
        val data = MutableLiveData<ClassResponse>()
        service.getDailyClass().enqueue(object : Callback<ClassResponse>{
            override fun onResponse(call: Call<ClassResponse>, response: Response<ClassResponse>) {
                if (response.body() != null){
                    data.value = response.body()
                } else {
                    TODO("Error handle")
                }
            }

            override fun onFailure(call: Call<ClassResponse>, t: Throwable) {
                TODO("Error handle")
            }
        })
        return data
    }
}