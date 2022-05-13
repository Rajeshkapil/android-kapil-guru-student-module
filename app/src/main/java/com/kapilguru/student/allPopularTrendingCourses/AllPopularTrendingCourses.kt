package com.kapilguru.student.allPopularTrendingCourses

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.allPopularTrendingCourses.viewModel.AllPopularTrendingCourseViewModel
import com.kapilguru.student.allPopularTrendingCourses.viewModel.AllPopularTrendingCourseViewModelFactory
import com.kapilguru.student.courseDetails.CourseDetailsActivity
import com.kapilguru.student.databinding.ActivityAllPopularTrendingCoursesBinding
import com.kapilguru.student.homeActivity.popularAndTrending.PopularAndTrendingAdapter
import com.kapilguru.student.homeActivity.popularAndTrending.PopularAndTrendingApi
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status

class AllPopularTrendingCourses : BaseActivity(), PopularAndTrendingAdapter.CardItem {
    lateinit var binding: ActivityAllPopularTrendingCoursesBinding
    private var item: ArrayList<PopularAndTrendingApi>?= null
    lateinit var adapter : PopularAndTrendingAdapter
    lateinit var viewModel: AllPopularTrendingCourseViewModel
    lateinit var dialog: CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.popular_and_trending))
        observeViewModelData()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_popular_trending_courses)
        dialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, AllPopularTrendingCourseViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(AllPopularTrendingCourseViewModel::class.java)
        adapter = PopularAndTrendingAdapter(this, true)
        binding.recy.adapter = adapter
    }

    private fun observeViewModelData() {
        viewModel.fetchAllPopularAndTrending()

        viewModel.popularAndTrendingResponse.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.popularAndTrendingApi?.let { it ->
                        if (it.isNotEmpty()) {
                            adapter.listItem = it
                        }
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    if(response.code == NETWORK_CONNECTIVITY_EROR) {
                        showSingleButtonErrorDialog(this,getString(R.string.network_connection_error))
                    }
                }
            }
        })
    }


    override fun onCardClick(popularAndTrendingApi: PopularAndTrendingApi) {
        startActivity(Intent(this@AllPopularTrendingCourses, CourseDetailsActivity::class.java).apply {
            putExtra(PARAM_COURSE_ID,popularAndTrendingApi.id.toString())
        })
    }
}