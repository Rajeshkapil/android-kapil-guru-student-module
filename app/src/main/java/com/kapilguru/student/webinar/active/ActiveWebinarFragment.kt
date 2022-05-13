package com.kapilguru.student.webinar.active

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kapilguru.student.*
import com.kapilguru.student.allTrendingDemos.AllTrendingDemosActivity
import com.kapilguru.student.allTrendingWebinars.AllTrendingWebinars
import com.kapilguru.student.databinding.FragmentActiveWebinarBinding
import com.kapilguru.student.webinar.model.ActiveWebinar
import com.kapilguru.student.webinar.viewModel.WebinarViewModel
import com.kapilguru.student.webinar.webinarDetails.WebinarDetailsActivity


class ActiveWebinarFragment : Fragment(), ActiveWebinarAdapter.ActiveWebinarClickListener {
    lateinit var binding: FragmentActiveWebinarBinding
    val viewModel: WebinarViewModel by activityViewModels()
    lateinit var adapter: ActiveWebinarAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActiveWebinarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        binding.noDataAvailable.tvNoData.text = "No Active Webinars"
        binding.noDataAvailable.btnEnrollNow.text = getString(R.string.enroll_now_pre_webinar_text)
        binding.noDataAvailable.btnEnrollNow.setOnClickListener(View.OnClickListener { naviagteToAllWebinars() })
        setAdapter()
        observeViewModelData()
        setEmptyViewText()
    }

    private fun setAdapter() {
        adapter = ActiveWebinarAdapter(this)
//        binding.rvActiveClasses.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvActiveClasses.adapter = adapter
    }

    /*Setting empty view text in OnCreate as the data is not being observed, might be because of observable data is empty or
   not set to mutable live data.*/
    private fun setEmptyViewText() {
        binding.noDataAvailable.tvNoData.text = getString(R.string.no_active_webinnars)
        binding.noDataAvailable.btnEnrollNow.visibility = View.VISIBLE
        binding.noDataAvailable.btnEnrollNow.text = getString(R.string.enroll_now_pre_webinar_text)
        binding.noDataAvailable.btnEnrollNow.setOnClickListener(View.OnClickListener { navigateToAllWebinars() })
    }

    private fun navigateToAllWebinars(){
        startActivity(Intent(requireContext(), AllTrendingWebinars::class.java))
    }

    private fun observeViewModelData() {
        viewModel.activeWebinarList.observe(viewLifecycleOwner, { activeWebinar ->
            //  adapter.setData(activeWebinar)
            setAdapterData(activeWebinar)
        })
    }

    private fun naviagteToAllWebinars() {
        startActivity(Intent(requireContext(), AllTrendingWebinars::class.java))
    }


    private fun setAdapterData(activeWebinar: ArrayList<ActiveWebinar>?) {
        if (activeWebinar?.isEmpty()!!) {
            binding.rvActiveClasses.visibility = View.GONE
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
            binding.noDataAvailable.btnEnrollNow.visibility = View.VISIBLE
        } else {
            binding.rvActiveClasses.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
            binding.noDataAvailable.btnEnrollNow.visibility = View.GONE
            adapter.setData(activeWebinar)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ActiveWebinarFragment().apply {

            }
    }

    override fun onShareClicked(activeWebinarData: ActiveWebinar) {
        shareIntent(BuildConfig.SHARE_URL + WEBINAR_DETAILS + activeWebinarData.webinarCode, requireContext())
    }

    override fun onViewMoreClicked(activeWebinarData: ActiveWebinar) {
        activeWebinarData.webinarId?.let { id ->
            WebinarDetailsActivity.launchActivity(requireActivity(), id)
        }
    }
}