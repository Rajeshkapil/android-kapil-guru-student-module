package com.kapilguru.student.homeActivity.popularAndTrending

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapilguru.student.*
import com.kapilguru.student.allPopularTrendingCourses.AllPopularTrendingCourses
import com.kapilguru.student.courseDetails.CourseDetailsActivity
import com.kapilguru.student.homeActivity.dashboard.DashBoardViewModel
import com.kapilguru.student.homeActivity.dashboard.DashBoardViewModelFactory
import com.kapilguru.student.homeActivity.models.AllDemosApi
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import kotlinx.android.synthetic.main.fragment_popular_and_trending.*
import kotlinx.android.synthetic.main.fragment_popular_and_trending.view.*


class PopularAndTrendingFragment : Fragment(),PopularAndTrendingAdapter.CardItem {
    lateinit var  viewModel: DashBoardViewModel
    lateinit var adapter: PopularAndTrendingAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular_and_trending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(this.requireContext())
        viewModel = ViewModelProvider(this.requireParentFragment(), DashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(DashBoardViewModel::class.java)
        setUpRecycler(view)
        viewModelObserver()
        clickListerens()
    }

    private fun clickListerens() {
        view_all.setOnClickListener {
            startActivity(Intent(this.requireContext(),AllPopularTrendingCourses::class.java).apply {
                putParcelableArrayListExtra(PARAM_ALL_POPULAR_TRENDING_LIST,viewModel.popularTrendingList)
            })
        }
    }

    private fun viewModelObserver() {
        viewModel.fetchAllPopularAndTrending()

        viewModel.popularAndTrendingResponse.observe(requireActivity(), Observer { response->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.popularAndTrendingApi?.let { it ->
                        adapter.listItem = it
                        viewModel.popularTrendingList = it as ArrayList<PopularAndTrendingApi>
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setUpRecycler(view: View) {
        adapter = PopularAndTrendingAdapter(this,false)
        view.recy.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        view.recy.adapter  = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = PopularAndTrendingFragment()
    }

    override fun onCardClick(popularAndTrendingApi: PopularAndTrendingApi) {
        startActivity(Intent(requireContext(), CourseDetailsActivity::class.java).apply {
            putExtra(PARAM_COURSE_ID,popularAndTrendingApi.id.toString())
        })
    }
}