package dk.joga.jogago

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.joga.jogago.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val service: WebService) {
    fun getWelcomePopup(token: String): LiveData<Resource<WelcomePopup>>{
        val data = MutableLiveData<Resource<WelcomePopup>>()
        val auth = "Bearer $token"
        service.getWelcomePopup().enqueue(object : Callback<WelcomePopupResponse>{
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

    fun getInstructors(): LiveData<Resource<List<Instructor>>> {
        val data = MutableLiveData<Resource<List<Instructor>>>()
        service.getInstructors().enqueue(object : Callback<Instructors>{
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
        service.getInstructor(id).enqueue(object : Callback<InstructorResponse>{
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

    fun getJourneys(): LiveData<Resource<List<Journey>>> {
        val data = MutableLiveData<Resource<List<Journey>>>()
        service.getJourneys().enqueue(object : Callback<Journeys>{
            override fun onResponse(call: Call<Journeys>, response: Response<Journeys>) {
                if (response.body() != null){
                    if (response.body()!!.journeys.isNotEmpty()) {
                        val resource = Resource(Status.Success, response.body()!!.journeys)
                        data.value = resource
                    } else {
                        val resource = Resource(Status.Empty, null)
                        data.value = resource
                    }
                } else {
                    val resource = Resource(Status.Fail, null)
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

    fun getJourney(id: String): LiveData<Resource<Journey>> {
        val data = MutableLiveData<Resource<Journey>>()
        service.getJourney(id).enqueue(object : Callback<JourneyResponse>{
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

    fun getCategories(): LiveData<Resource<List<Category>>> {
        val data = MutableLiveData<Resource<List<Category>>>()
        service.getCategories().enqueue(object : Callback<Categories>{
            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                if (response.body()!!.categories.isEmpty()){
                    val resource = Resource(Status.Empty, listOf<Category>())
                    data.value = resource
                } else {
                    val resource = Resource(Status.Success, response.body()!!.categories)
                    data.value = resource
                }
            }
            override fun onFailure(call: Call<Categories>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun getClasses(token: String, categoryId: String = ""): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        val auth = "Bearer $token"
        service.getClasses(categoryId).enqueue(object : Callback<Classes>{
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

    fun getNewClasses(): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        service.getNewClasses().enqueue(object : Callback<Classes>{
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

    fun getLikedClasses(): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        service.getLikedClasses().enqueue(object : Callback<Classes>{
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
        service.getInstructorClasses(instructorId).enqueue(object : Callback<Classes>{
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

    fun getClass(id: String): LiveData<Resource<Class>> {
        val data = MutableLiveData<Resource<Class>>()
        service.getClass(id).enqueue(object : Callback<ClassResponse>{
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

    fun toggleClassLike(id: String): LiveData<Resource<Void>>{
        val data = MutableLiveData<Resource<Void>>()
        service.toggleLike(id).enqueue(object : Callback<Void>{
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

    fun markClassAsWatched(classId: String): LiveData<Resource<Void>>{
        val data = MutableLiveData<Resource<Void>>()
        service.markClassAsWatched(classId).enqueue(object : Callback<Void>{
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
        service.logout().enqueue(object : Callback<Void>{
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