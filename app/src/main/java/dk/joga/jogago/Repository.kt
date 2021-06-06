package dk.joga.jogago

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.joga.jogago.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val serviceJoGa: ServiceJoGa) {
    fun getInstructors(): LiveData<Resource<List<Instructor>>> {
        val data = MutableLiveData<Resource<List<Instructor>>>()
        serviceJoGa.getInstructors().enqueue(object : Callback<Instructors>{
            override fun onResponse(call: Call<Instructors>, response: Response<Instructors>) {
                val resource = when (response.code()) {
                    200 -> {
                        if (response.body()!!.totalCount != 0) {
                            Resource(Status.Success, response.body()!!.instructors, null, response.body()!!.totalCount)
                        } else {
                            Resource(Status.Empty, null)
                        }
                    }
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
            }
            override fun onFailure(call: Call<Instructors>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun getInstructor(id: String): LiveData<Resource<Instructor>> {
        val data = MutableLiveData<Resource<Instructor>>()
        serviceJoGa.getInstructor(id).enqueue(object : Callback<InstructorResponse>{
            override fun onResponse(call: Call<InstructorResponse>, response: Response<InstructorResponse>) {
                val resource = when (response.code()) {
                    200 -> Resource(Status.Success, response.body()!!.instructor)
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
            }

            override fun onFailure(call: Call<InstructorResponse>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getJourneys(page: Int = 1): LiveData<Resource<List<Journey>>> {
        val data = MutableLiveData<Resource<List<Journey>>>()
        serviceJoGa.getJourneys(page).enqueue(object : Callback<Journeys>{
            override fun onResponse(call: Call<Journeys>, response: Response<Journeys>) {
                val resource = when (response.code()) {
                    200 -> {
                        if (response.body()!!.totalCount != 0) {
                            Resource(Status.Success, response.body()!!.journeys, null, response.body()!!.totalCount)
                        } else {
                            Resource(Status.Empty, null)
                        }
                    }
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
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
        serviceJoGa.getJourney(id).enqueue(object : Callback<JourneyResponse>{
            override fun onResponse(call: Call<JourneyResponse>, response: Response<JourneyResponse>) {
                val resource = when (response.code()) {
                    200 -> Resource(Status.Success, response.body()!!.journey)
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
            }

            override fun onFailure(call: Call<JourneyResponse>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getCategories(page: Int = 1): LiveData<Resource<List<Category>>> {
        val data = MutableLiveData<Resource<List<Category>>>()
        serviceJoGa.getCategories(page).enqueue(object : Callback<Categories>{
            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                val resource = when (response.code()) {
                    200 -> {
                        if (response.body()!!.totalCount != 0) {
                            Resource(Status.Success, response.body()!!.categories, null, response.body()!!.totalCount)
                        } else {
                            Resource(Status.Empty, null)
                        }
                    }
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
            }
            override fun onFailure(call: Call<Categories>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun getCategoryClasses(id: String, page: Int = 1): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        serviceJoGa.getClasses(id, page).enqueue(object : Callback<Classes>{
            override fun onResponse(call: Call<Classes>, response: Response<Classes>) {
                val resource = when (response.code()) {
                    200 -> {
                        if (response.body()!!.totalCount != 0) {
                            Resource(Status.Success, response.body()!!.classes, null, response.body()!!.totalCount)
                        } else {
                            Resource(Status.Empty, null)
                        }
                    }
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
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
        serviceJoGa.getNewClasses().enqueue(object : Callback<Classes>{
            override fun onResponse(call: Call<Classes>, response: Response<Classes>) {
                val resource = when (response.code()) {
                    200 -> {
                        if (response.body()!!.totalCount != 0) {
                            Resource(Status.Success, response.body()!!.classes, null, response.body()!!.totalCount)
                        } else {
                            Resource(Status.Empty, null)
                        }
                    }
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
            }

            override fun onFailure(call: Call<Classes>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getLikedClasses(page: Int = 1): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        serviceJoGa.getLikedClasses(page).enqueue(object : Callback<Classes>{
            override fun onResponse(call: Call<Classes>, response: Response<Classes>) {
                val resource = when (response.code()) {
                    200 -> {
                        if (response.body()!!.totalCount != 0) {
                            Resource(Status.Success, response.body()!!.classes, null, response.body()!!.totalCount)
                        } else {
                            Resource(Status.Empty, null)
                        }
                    }
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
            }

            override fun onFailure(call: Call<Classes>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun getInstructorClasses(instructorId: String, page: Int = 1): LiveData<Resource<List<Class>>> {
        val data = MutableLiveData<Resource<List<Class>>>()
        serviceJoGa.getInstructorClasses(instructorId, page).enqueue(object : Callback<Classes>{
            override fun onResponse(call: Call<Classes>, response: Response<Classes>) {
                val resource = when (response.code()) {
                    200 -> {
                        if (response.body()!!.totalCount != 0) {
                            Resource(Status.Success, response.body()!!.classes, null, response.body()!!.totalCount)
                        } else {
                            Resource(Status.Empty, null)
                        }
                    }
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
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
        serviceJoGa.getDailyClass().enqueue(object : Callback<ClassResponse>{
            override fun onResponse(call: Call<ClassResponse>, response: Response<ClassResponse>) {
                val resource = when (response.code()) {
                    200 -> Resource(Status.Success, response.body()!!.lecture)
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
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
        serviceJoGa.getClass(id).enqueue(object : Callback<ClassResponse>{
            override fun onResponse(call: Call<ClassResponse>, response: Response<ClassResponse>) {
                val resource = when (response.code()) {
                    200 -> Resource(Status.Success, response.body()!!.lecture)
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
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
        serviceJoGa.login(username, password).enqueue(object : Callback<Login>{
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                val resource = when (response.code()) {
                    201 -> Resource(Status.Success, response.body()!!)
                    401 -> {
                        if (response.errorBody() != null) {
                            if (response.errorBody()!!.string().equals("Inactive subscription.")){
                                Resource(Status.SubscriptionEnded, null)
                            } else {
                                Resource(Status.Unauthorized, null)
                            }
                        } else {
                            Resource(Status.Unauthorized, null)
                        }
                    }
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
            }
            override fun onFailure(call: Call<Login>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

    fun syncLogin(username: String, password: String): Resource<Login> {
        lateinit var data: Resource<Login>
        val response = serviceJoGa.login(username, password).execute().body()
        data = if (response != null) {
            Resource(Status.Success, response)
        } else {
            Resource(Status.Fail, null)
        }
        return data
    }

    fun refreshToken(token: String, refreshToken: String): Resource<Login>{
        lateinit var data: Resource<Login>
        val response = serviceJoGa.login(token, refreshToken).execute().body()
        data = if (response != null) {
            Resource(Status.Success, response)
        } else {
            Resource(Status.Fail, null)
        }
        return data
    }

    fun toggleClassLike(id: String): LiveData<Resource<Void>>{
        val data = MutableLiveData<Resource<Void>>()
        serviceJoGa.toggleLike(id).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val resource = when (response.code()) {
                    201 -> Resource(Status.Success, response.body()!!)
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
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
        serviceJoGa.markClassAsWatched(classId).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val resource = when (response.code()) {
                    201 -> Resource(Status.Success, response.body()!!)
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                val resource = Resource(Status.Success, null, t)
                data.value = resource
            }

        })
        return data
    }

    fun logout(): LiveData<Resource<Void>>{
        val data = MutableLiveData<Resource<Void>>()
        serviceJoGa.logout().enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val resource = when (response.code()) {
                    200 -> Resource(Status.Success, response.body())
                    401 -> Resource(Status.Unauthorized, null)
                    403 -> Resource(Status.SubscriptionEnded, null)
                    else -> Resource(Status.Fail, null)
                }
                data.value = resource
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                val resource = Resource(Status.Fail, null, t)
                data.value = resource
            }
        })
        return data
    }

//    fun getWelcomePopup(): LiveData<Resource<WelcomePopup>>{
//        val data = MutableLiveData<Resource<WelcomePopup>>()
//        service.getWelcomePopup().enqueue(object : Callback<WelcomePopupResponse>{
//            override fun onResponse(call: Call<WelcomePopupResponse>, response: Response<WelcomePopupResponse>) {
//                if (response.body() != null){
//                    val resource = Resource(Status.Success, response.body()!!.welcomePopup)
//                    data.value = resource
//                } else {
//                    val resource = Resource(Status.Empty, null)
//                    data.value = resource
//                }
//            }
//            override fun onFailure(call: Call<WelcomePopupResponse>, t: Throwable) {
//                val resource = Resource(Status.Fail, null, t)
//                data.value = resource
//            }
//
//        })
//        return data
//    }
}