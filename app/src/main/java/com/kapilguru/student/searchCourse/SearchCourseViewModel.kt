package com.kapilguru.student.searchCourse


 import android.app.Application
 import androidx.lifecycle.*
 import com.kapilguru.student.network.ApiResource
 import com.kapilguru.student.network.CommonResponse
 import com.kapilguru.student.network.RetrofitNetwork
 import com.kapilguru.student.searchCourse.model.*
 import kotlinx.coroutines.Dispatchers
 import kotlinx.coroutines.launch
 import retrofit2.HttpException
 import java.io.IOException
 import kotlin.collections.ArrayList


data class SearchCourseViewModel(private val searchCourseRepository: SearchCourseRepository, var app: Application) : AndroidViewModel(app) {


    var courseFee: MutableLiveData<COURSEFEE> = MutableLiveData(COURSEFEE.ALL)
    var duration: DURATION = DURATION.ALL
    var batchType: BATCHTYPE = BATCHTYPE.ALL
    var bestTrainerFilter: Boolean = false// by default -1 not set

    var searchText: MutableLiveData<String> = MutableLiveData()
    var autoSearchModelResponse: MutableLiveData<ApiResource<AutoSearchModelResponse>> = MutableLiveData()
    // response From Api
    var selectedSearchCourseResponse: MutableLiveData<ApiResource<SelectedSearchCourseResponse>> = MutableLiveData()
    // structured data
    var finalSearchResult: MutableLiveData<ArrayList<PositionArrayItem>> = MutableLiveData(arrayListOf())
    var applyFilter:  MutableLiveData<Boolean> = MutableLiveData()

    var notifyNewKeyWordResponse: MutableLiveData<ApiResource<NotifyNewKeyWordResponse>> = MutableLiveData()

    var createLeadRequest: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()

    init {

    }


    fun getSearchResult(text: String) {
        autoSearchModelResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                autoSearchModelResponse.postValue(
                    ApiResource.success(searchCourseRepository.searchCourse(text))
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                autoSearchModelResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code)
                )
            }  catch (exception: HttpException) {
                autoSearchModelResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                autoSearchModelResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun fetchSelectedCourse(courseTitle: String) {
        val input = SelectedSearchCourseRequest(courseTitle)
        selectedSearchCourseResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                selectedSearchCourseResponse.postValue(
                    ApiResource.success(searchCourseRepository.selectedSearchCourse(input))
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                selectedSearchCourseResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code)
                )
            } catch (exception: HttpException) {
                selectedSearchCourseResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                selectedSearchCourseResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun applyFilter(): ArrayList<PositionArrayItem> {
      val  originalData = finalSearchResult.value as ArrayList<PositionArrayItem>

        val filterData = originalData.filter { it ->
            var minDay:Int = 0
            var maxDay:Int = 100
            when(duration.days) {
                30 -> {
                    minDay = 0
                    maxDay = 30
                }
                60 -> {
                    minDay = 31
                    maxDay = 60
                }
                90 -> {
                    minDay = 61
                    maxDay = 30
                }
                91 -> {
                    minDay = 91
                    maxDay = 100
                }
            }
              it.durationDays in minDay..maxDay
               && batchTypeValidation(it)
                      && bestTrainerValidation(it)
           }
        val finalList :ArrayList<PositionArrayItem> = ArrayList()
        return if(courseFee.value == COURSEFEE.LOWTOHIGH) {
            val abc  = filterData.sortedBy { it.fee }
            finalList.addAll(abc)
            finalList
        } else {
            val abc = filterData.sortedByDescending { it.fee }.reversed()
            finalList.addAll(abc)
            finalList
        }
    }

    private fun batchTypeValidation(it: PositionArrayItem) :Boolean {
        return if(batchType.batchDay == BATCHTYPE.ALL.batchDay ) {
            true
        } else {
            batchType.batchDay.equals(it.batchtype,true)
        }
//        it.batchtype.equals(batchType.batchDay, ignor eCase = true)
    }

    private fun bestTrainerValidation(it: PositionArrayItem): Boolean {
        return if(bestTrainerFilter) {
            (it.courseBadgeId!=null)
        } else {
            true
        }
    }


    fun applyTeacherFilter(trainerName: String): ArrayList<PositionArrayItem> {
        val  originalData = finalSearchResult.value as ArrayList<PositionArrayItem>
        return originalData.filter { it ->
            it.trainerName.toLowerCase().contains(trainerName,true)
        } as ArrayList<PositionArrayItem>
    }

    fun notifyNewKeyWord(newKeyWord: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                notifyNewKeyWordResponse.postValue(
                    ApiResource.success(searchCourseRepository.notifyNewKeyWord(NotifyNewKeyWordRequest(newKeyWord)))
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                notifyNewKeyWordResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code)
                )
            } catch (exception: HttpException) {
                notifyNewKeyWordResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                notifyNewKeyWordResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun createLead(newKeyWord: CreateSearchLeadRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                createLeadRequest.postValue(
                    ApiResource.success(searchCourseRepository.createLeadRequest(newKeyWord))
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                createLeadRequest.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code)
                )
            } catch (exception: HttpException) {
                createLeadRequest.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                createLeadRequest.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }
}


enum class COURSEFEE {
    ALL,
    LOWTOHIGH,
    HIGHTOLOW,
}
enum class DURATION(var days:Int) {
    ALL(100),
    LESSTHAN30Days(30),
    THIRTY1TO60DAYS(60),
    SIXTY1TO90DAYS(90),
    GREATERTHAN90DAYS(91)
}

enum class BATCHTYPE(val batchDay: String) {
    ALL("both"),
    WEEKDAY("weekday"),
    WEEKEND("weekend"),
}
