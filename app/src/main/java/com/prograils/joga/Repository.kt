package com.prograils.joga

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prograils.joga.api.Instructors
import com.prograils.joga.api.Journeys
import com.prograils.joga.api.WebService
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
                data.value = response.body()
            }
            override fun onFailure(call: Call<Instructors>, t: Throwable) {

            }
        })
        return data
    }

    fun getJourneys(): LiveData<Journeys> {
        val data = MutableLiveData<Journeys>()
        service.getJourneys().enqueue(object : Callback<Journeys>{
            override fun onResponse(call: Call<Journeys>, response: Response<Journeys>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<Journeys>, t: Throwable) {

            }

        })
        return data
    }
}