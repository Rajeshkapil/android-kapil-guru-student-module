package com.kapilguru.student.homeActivity.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.R
import com.kapilguru.student.emailValidation
import com.kapilguru.student.homeActivity.models.*
import com.kapilguru.student.homeActivity.popularAndTrending.PopularAndTrendingApi
import com.kapilguru.student.homeActivity.popularAndTrending.PopularAndTrendingResponse
import com.kapilguru.student.isValidMobileNo
import com.kapilguru.student.jobOpenings.model.JobOpeningsData
import com.kapilguru.student.jobOpenings.model.JobOpeningsResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.CommonResponse
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.ui.home.UpComingScheduleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import retrofit2.HttpException
import java.io.IOException

class DashBoardViewModel(val homeScreenRepository:HomeScreenRepository, val context: Application,) : AndroidViewModel(context) {

    lateinit var popularTrendingList: ArrayList<PopularAndTrendingApi>
    lateinit var trendingWebinarsList: ArrayList<AllWebinarsApi>
    lateinit var trendingDemosList: ArrayList<AllDemosApi>
    lateinit var trendingJobsList: ArrayList<JobOpeningsData>
    val listOfHomeTopItems: MutableLiveData<MutableList<DashBoardViewPagerItem>> = MutableLiveData()
    val listOfHomeItems: MutableLiveData<MutableList<DashBoardItem>> = MutableLiveData()
    val listOfTabItems: MutableLiveData<MutableList<DashBoardCustomTabModel>> = MutableLiveData()
    val upcomingResponse: MutableLiveData<ApiResource<UpComingScheduleResponse>> = MutableLiveData()
    val topCategories: MutableLiveData<ApiResource<TopCategoriesResponse>> = MutableLiveData()
    val allWebinarsData: MutableLiveData<ApiResource<AllWebinarsResponse>> = MutableLiveData()
    val allJobOpeningsData: MutableLiveData<ApiResource<JobOpeningsResponse>> = MutableLiveData()
    val popularAndTrendingResponse: MutableLiveData<ApiResource<PopularAndTrendingResponse>> = MutableLiveData()
    val alDemosData: MutableLiveData<ApiResource<AllDemosResponse>> = MutableLiveData()
    val whyKapilGuruData: MutableLiveData<MutableList<DashBoardItem>> = MutableLiveData()
    val createLeadRequest: MutableLiveData<CreateLeadRequest> = MutableLiveData(CreateLeadRequest())
    val commonResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()
    val createLeadError: MutableLiveData<String> = MutableLiveData()

    fun setHomeTopItems() {
        val dashBoardViewPagerItem = mutableListOf<DashBoardViewPagerItem>()
        dashBoardViewPagerItem.add(DashBoardViewPagerItem().apply {
            image = R.drawable.home_viewpager_popular_trending
            title = "Popular & Trending\nCourses"
            backgroundcolor = R.color.home_top_bg1
        })
        dashBoardViewPagerItem.add(DashBoardViewPagerItem().apply {
            image = R.drawable.home_viewpager_webinar
            title = "Enroll\nLive Webinars"
            backgroundcolor = R.color.home_top_bg2
        })
        dashBoardViewPagerItem.add(DashBoardViewPagerItem().apply {
            image = R.drawable.home_viewpager_demo_lectures
            title = "Enroll Free\nLectures"
            backgroundcolor = R.color.home_top_bg3
        })
        listOfHomeTopItems.postValue(dashBoardViewPagerItem)
    }

    fun setHomeItems() {
        val homeItems = mutableListOf<DashBoardItem>()

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_course
            title = "Free\nLectures"
        })
        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_course
            title = "Webinars"
        })
        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_course
            title = "Trending\nCourses"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_course
            title = "My Class Room"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_demo_lectures
            title = "Upcoming\nSchedule"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_demo_lectures
            title = "My Free\nLectures"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_webinar
            title = "My Webinars"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_exams
            title = "Exams"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_completed_request
            title = "Certificates"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_job_openings
            title = "Job openings"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_messages
            title = "Messages"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_wallet
            title = "My Wallet"
        })



        /*homeItems.add(DashBoardItem().apply {
            image = R.drawable.ic_completed_request
            title = "completed\nReports"
        })*/
        listOfHomeItems.postValue(homeItems)
    }

    fun setDashBoardTabsItems() {
        val tabsData = mutableListOf<DashBoardCustomTabModel>()
        tabsData.add(DashBoardCustomTabModel().apply {
            title = "TRENDING"
            subTitle = "WEBINARS"
            image = R.drawable.ic_girl_in_cirle_one
        })

        tabsData.add(DashBoardCustomTabModel().apply {
            title = "TRENDING"
            subTitle = "Free Lectures"
            image = R.drawable.ic_girl_in_circle_two
        })

        tabsData.add(DashBoardCustomTabModel().apply {
            title = "VIEW & APPLY"
            subTitle = "JOB OPENINGS"
            image = R.drawable.ic_girl_in_cirle_one
        })

        listOfTabItems.postValue(tabsData)
    }


    fun setWhyKapilGuruItems() {
        val homeItems = mutableListOf<DashBoardItem>()

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.online_class
            title = "Live Online Classes"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.live_recording
            title = "Placement Assistance"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.refund
            title = "Get Refund"
        })
        homeItems.add(DashBoardItem().apply {
            image = R.drawable.certificate
            title = "Certification"
        })

        homeItems.add(DashBoardItem().apply {
            image = R.drawable.graduate
            title = "Limited Students"
        })
        homeItems.add(DashBoardItem().apply {
            image = R.drawable.trainer
            title = "Find The Right Trainer"
        })
        homeItems.add(DashBoardItem().apply {
            image = R.drawable.security
            title = "Convenient & Safe"
        })
        homeItems.add(DashBoardItem().apply {
            image = R.drawable.twnety_four_hours
            title = "24/7 Support"
        })

        whyKapilGuruData.value =(homeItems)
    }

    fun fetchUpcomingSchedule() {
        val userId = StorePreferences(context).studentId
        upcomingResponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                upcomingResponse.postValue(
                    ApiResource.success(
                        homeScreenRepository.fetchUpcomingSchedule(userId = userId.toString())
                    )
                )
            }
            catch (exception: RetrofitNetwork.NetworkConnectionError) {
                upcomingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", code = exception.code)
                )
            } catch (exception: IOException) {
                upcomingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: HttpException) {
                upcomingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun fetchTopCategories() {
        val userId = StorePreferences(context).studentId
        topCategories.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                topCategories.postValue(
                    ApiResource.success(
                        homeScreenRepository.fetchTopCategories()
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                topCategories.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            } catch (exception: IOException) {
                topCategories.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                topCategories.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun fetchAllWebinars() {
        val userId = StorePreferences(context).studentId
        allWebinarsData.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                allWebinarsData.postValue(
                    ApiResource.success(
                        homeScreenRepository.fetchAllWebinars()
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                allWebinarsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            } catch (exception: IOException) {
                allWebinarsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                allWebinarsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun fetchAllDemos() {
        val userId = StorePreferences(context).studentId
        alDemosData.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                alDemosData.postValue(
                    ApiResource.success(
                        homeScreenRepository.fetchAllDemos()
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                alDemosData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code)
                )
            } catch (exception: IOException) {
                alDemosData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                alDemosData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun fetchAllJobOpenings() {
        allJobOpeningsData.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                allJobOpeningsData.postValue(
                    ApiResource.success(
                        homeScreenRepository.fetchAllJobOpenings()
                    )
                )
            }
            catch (exception: RetrofitNetwork.NetworkConnectionError) {
                allJobOpeningsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            } catch (exception: IOException) {
                allJobOpeningsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                allJobOpeningsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun fetchAllPopularAndTrending() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                popularAndTrendingResponse.postValue(
                    ApiResource.success(
                        homeScreenRepository.getDashBoardPopularAndTrendingCourses()
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                popularAndTrendingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            }catch (exception: IOException) {
                popularAndTrendingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                popularAndTrendingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun createLeadApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                commonResponse.postValue(
                    ApiResource.success(
                        homeScreenRepository.createLeadApi(createLeadRequest.value!!)
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                commonResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: HttpException) {
                commonResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                commonResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun validateCreateLead(): Boolean {
        when {
           /* createLeadRequest.value!!.fullName.isNullOrBlank() -> {
                createLeadError.value = "Please Enter Full  Name"
                return false
            }
            !createLeadRequest.value!!.contactNumber!!.isValidMobileNo() -> {
                createLeadError.value = "Please Enter Valid Mobile Number"
                return false
            }
            createLeadRequest.value!!.emailId!!.emailValidation() -> {
                createLeadError.value = "Please Enter Valid email "
                return false
            }*/
            createLeadRequest.value!!.message.isNullOrBlank() -> {
                createLeadError.value = "Message Can't be Empty"
                return false
            }
            else -> {
                createLeadRequest.value!!.fullName = StorePreferences(context).userName
                createLeadRequest.value!!.contactNumber = StorePreferences(context).contactNumber
                createLeadRequest.value!!.emailId = StorePreferences(context).email
                return true
            }


        }
    }


}