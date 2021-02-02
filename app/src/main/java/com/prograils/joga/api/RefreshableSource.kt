package com.prograils.joga.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

abstract class RefreshableSource <T> {
    private val mediator: MediatorLiveData<Resource<T>> = MediatorLiveData()
    private var lastData: LiveData<Resource<T>>? = null

    abstract fun provideLiveData(): LiveData<Resource<T>>

    fun refresh() {
        val newData = provideLiveData()

        mediator.addSource(newData) { t: Resource<T>? ->
            mediator.value = t
            lastData?.let {
                mediator.removeSource(it)
                lastData = newData
            }
        }
    }

    fun getData(): LiveData<Resource<T>>{
        return mediator
    }
}