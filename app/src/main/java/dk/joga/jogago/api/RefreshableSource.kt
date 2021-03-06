package dk.joga.jogago.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

abstract class RefreshableSource <T> {
    private val mediator: MediatorLiveData<Resource<T>> = MediatorLiveData()
    private var lastData: LiveData<Resource<T>>? = null
    var page: Int = 1

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