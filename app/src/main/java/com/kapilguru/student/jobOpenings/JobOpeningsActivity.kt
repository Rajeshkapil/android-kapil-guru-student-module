package com.kapilguru.student.jobOpenings

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityJobOpeningsBinding
import com.kapilguru.student.jobOpenings.apply.ApplyJobOpeningsActivity
import com.kapilguru.student.jobOpenings.model.JobOpeningsData
import com.kapilguru.student.jobOpenings.viewmodel.JobOpeningsViewModel
import com.kapilguru.student.jobOpenings.viewmodel.JobOpeningsViewModelFactory
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status

class JobOpeningsActivity : BaseActivity(), JobOpeningsAdapter.JobOpeningsClickListener{
    lateinit var binding: ActivityJobOpeningsBinding
    lateinit var viewModel: JobOpeningsViewModel
    private var jobOpeningsDataList: ArrayList<JobOpeningsData>? = null
    lateinit var jobAdapter: JobOpeningsAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_openings)
        viewModel = ViewModelProvider(this, JobOpeningsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(JobOpeningsViewModel::class.java)
        binding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.job_openings))
        viewModelObserver()
    }

    fun viewModelObserver() {
        viewModel.fetchAllJobOpenings()
        viewModel.allJobOpeningsData.observe(this, Observer { response->
            when (response.status) {

                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.data?.let { data ->
                        viewModel.trendingJobsList = data as ArrayList<JobOpeningsData>
                        if (data.isNotEmpty())
                        {
                            binding.rvJobOpeningsList.visibility = View.VISIBLE
                            binding.noDataAvailable.tvNoData.visibility = View.GONE
                            binding.noDataAvailable.btnEnrollNow.visibility = View.GONE
                            showJobOpeningsRecycler(data)
                        }else{
                            binding.rvJobOpeningsList.visibility = View.GONE
                            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
                            binding.noDataAvailable.btnEnrollNow.visibility = View.VISIBLE
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

    private fun showJobOpeningsRecycler(data: ArrayList<JobOpeningsData>) {
        jobAdapter = JobOpeningsAdapter(this)
        data.let {
            jobAdapter.setData(it)
            binding.rvJobOpeningsList.adapter = jobAdapter
        }
    }

    override fun onItemClicked(jobOpeningsData: JobOpeningsData) {
        ApplyJobOpeningsActivity.launchActivity(this,jobOpeningsData)
    }
}