package com.kapilguru.student.homeActivity.dashboard.tabFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapilguru.student.*
import com.kapilguru.student.demoLecture.model.OnGoingDemoLectures
import com.kapilguru.student.homeActivity.dashboard.DashBoardViewModel
import com.kapilguru.student.homeActivity.dashboard.DashBoardViewModelFactory
import com.kapilguru.student.homeActivity.models.AllWebinarsApi
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.webinar.webinarDetails.WebinarDetailsActivity
import kotlinx.android.synthetic.main.fragment_trending_webinars.view.*



/**
 * A simple [Fragment] subclass.
 * Use the [TrendingWebinars.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrendingWebinars : Fragment(), TrendingWebinarAdapter.TrendingWebinarCardClick {

    lateinit var  viewModel: DashBoardViewModel
    lateinit var trendingWebinarAdapter: TrendingWebinarAdapter
    lateinit var dialog: CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending_webinars, container, false);
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(this.requireContext())
        trendingWebinarAdapter = TrendingWebinarAdapter(this,false)

        viewModel = ViewModelProvider(this.requireParentFragment(), DashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(DashBoardViewModel::class.java)
//        viewModel = ViewModelProviders.of(parentFragment!!).get(DashBoardViewModel::class.java)


        setUPRecycler(view)
        viewModelObserver()
    }

    private fun setUPRecycler(view: View) {
        view.trendingWebinarsRecy.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        view.trendingWebinarsRecy.adapter  = trendingWebinarAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrendingWebinars()
    }


   fun viewModelObserver() {

       viewModel.fetchAllWebinars()

       viewModel.allWebinarsData.observe(requireActivity(), Observer { response->
           when (response.status) {

               Status.LOADING -> {
                   dialog.showLoadingDialog()
               }

               Status.SUCCESS -> {
                   response.data?.data?.let { upComingSchedule ->
                       trendingWebinarAdapter.upComingScheduleApiList = upComingSchedule
                       viewModel.trendingWebinarsList = upComingSchedule as ArrayList<AllWebinarsApi>
                   }
                   dialog.dismissLoadingDialog()
               }

               Status.ERROR -> {
                   dialog.dismissLoadingDialog()
               }
           }
       })

   }


    override fun onTrendingWebinarCardClick(webinarDetails: AllWebinarsApi) {
        webinarDetails.id?.let { id->
            WebinarDetailsActivity.launchActivity(requireActivity(), id)
        }
    }

    override fun onShareClick(webinarDetails: AllWebinarsApi) {
            shareIntent(BuildConfig.SHARE_URL + WEBINAR_DETAILS + webinarDetails.code, requireContext())
    }

}