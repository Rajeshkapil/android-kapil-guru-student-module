package com.kapilguru.student.topCategories.selectedTopCategory

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivitySelectedTopCategoryBinding
import com.kapilguru.student.homeActivity.models.TopCategoriesApi
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.searchCourse.SearchCourseActivity
import com.kapilguru.student.topCategories.selectedTopCategory.model.SelectedTopCategoryCourseResData
import com.kapilguru.student.topCategories.selectedTopCategory.viewModel.SelectedTopCategoryViewModel
import com.kapilguru.student.topCategories.selectedTopCategory.viewModel.SelectedTopCategoryViewModelFactory

class SelectedTopCategoryActivity : AppCompatActivity(), SelectedTopCategoryCourseAdapter.CourseClickListener {
    private val TAG = "SelectedTopCategoryActivity"
    lateinit var binding: ActivitySelectedTopCategoryBinding
    lateinit var viewModel: SelectedTopCategoryViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: SelectedTopCategoryCourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVar()
        getIntentData()
        setActivityName()
        setClickListeners()
        observeViewModelData()
        viewModel.getCourseByCategory()
    }

    private fun initLateInitVar() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_selected_top_category)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, SelectedTopCategoryViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(SelectedTopCategoryViewModel::class.java)
        adapter = SelectedTopCategoryCourseAdapter(this)
        binding.rvCourse.adapter = adapter
    }

    private fun getIntentData() {
        viewModel.selectedCategory = intent.getParcelableExtra(SELECTED_CATEGORY)!!
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = viewModel.selectedCategory.categoryName + " " + getString(R.string.top_categories)
    }

    private fun setClickListeners() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
    }

    fun observeViewModelData() {
        viewModel.courseListApiRes.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.data?.let { courses ->
                        setAdapterData(courses)
                    }
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(response.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun setAdapterData(courses: ArrayList<SelectedTopCategoryCourseResData>) {
        if (courses.isEmpty()) {
            binding.rvCourse.visibility = View.GONE
            binding.noDataAvailable.root.visibility = View.VISIBLE
        } else {
            adapter.setData(courses)
        }
    }

    override fun onCourseClicked(course: SelectedTopCategoryCourseResData) {
        startActivity(Intent(this,SearchCourseActivity::class.java).apply {
            putExtra(PARAM_IS_FROM_DASHBOARD,course.courseTitle)
        })
    }

    companion object {
        const val SELECTED_CATEGORY = "SELECTED_CATEGORY"

        fun launchActivity(category: TopCategoriesApi, activity: Activity) {
            val intent = Intent(activity, SelectedTopCategoryActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelable(SELECTED_CATEGORY, category)
                }
                putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }
}