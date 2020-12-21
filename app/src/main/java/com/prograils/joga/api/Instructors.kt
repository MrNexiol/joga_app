package com.prograils.joga.api

data class Instructors(private val instructors: List<Instructor>) {
    override fun toString(): String {
        var answer = ""
        for (instructor in instructors){
            answer += "$instructor\n"
        }
        return answer
    }
}