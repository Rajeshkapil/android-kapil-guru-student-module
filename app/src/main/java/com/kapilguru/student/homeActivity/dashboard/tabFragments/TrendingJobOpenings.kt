package com.kapilguru.student.homeActivity.dashboard.tabFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapilguru.student.ACTIVE_JOB_APPLY
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.R
import com.kapilguru.student.homeActivity.dashboard.DashBoardViewModel
import com.kapilguru.student.homeActivity.dashboard.DashBoardViewModelFactory
import com.kapilguru.student.homeActivity.models.AllWebinarsApi
import com.kapilguru.student.jobOpenings.apply.ApplyJobOpeningsActivity
import com.kapilguru.student.jobOpenings.model.JobOpeningsData
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import kotlinx.android.synthetic.main.fragment_trending_job_openings.view.*
import kotlinx.android.synthetic.main.fragment_trending_webinars.view.*



/**
 * A simple [Fragment] subclass.
 * Use the [TrendingJobOpenings.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrendingJobOpenings : Fragment(), TrendingJobOpeningsAdapter.TrendingJobsCardClick {

    lateinit var  viewModel: DashBoardViewModel
    lateinit var trendingJobOpeningsAdapter: TrendingJobOpeningsAdapter
    lateinit var dialog: CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending_job_openings, container, false);
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(this.requireContext())
        trendingJobOpeningsAdapter = TrendingJobOpeningsAdapter(this,false)

        viewModel = ViewModelProvider(this.requireParentFragment(), DashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(DashBoardViewModel::class.java)
//        viewModel = ViewModelProviders.of(parentFragment!!).get(DashBoardViewModel::class.java)


        setUPRecycler(view)
        viewModelObserver()
    }

    private fun setUPRecycler(view: View) {
        view.rv_trending_jobs.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        view.rv_trending_jobs.adapter  = trendingJobOpeningsAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrendingJobOpenings()
    }


   fun viewModelObserver() {

       viewModel.fetchAllJobOpenings()

       viewModel.allJobOpeningsData.observe(requireActivity(), Observer { response->
           when (response.status) {

               Status.LOADING -> {
                   dialog.showLoadingDialog()
               }

               Status.SUCCESS -> {
                   response.data?.data?.let { data ->
                       trendingJobOpeningsAdapter.jobOpeningsDataList = data
                       viewModel.trendingJobsList = data as ArrayList<JobOpeningsData>
                   }
                   dialog.dismissLoadingDialog()
               }

               Status.ERROR -> {
                   dialog.dismissLoadingDialog()
               }
           }
       })

   }

    override fun onTrendingJobCardClick(jobOpeningsData: JobOpeningsData) {
        startActivity(Intent(requireActivity(), ApplyJobOpeningsActivity::class.java).apply {
            arguments = Bundle().apply {
                putExtra(ACTIVE_JOB_APPLY,jobOpeningsData)
            }
        })
    }


}