package com.prograils.joga.api

data class Resource<out T>(val status: Status, val data: T?, val error: Throwable? = null)