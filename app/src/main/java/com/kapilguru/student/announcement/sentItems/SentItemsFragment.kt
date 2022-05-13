package com.kapilguru.student.announcement.sentItems

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.R
import com.kapilguru.student.announcement.sentItems.data.SentItemsData
import com.kapilguru.student.announcement.sentItems.viewModel.SentItemsViewModel
import com.kapilguru.student.announcement.sentItems.viewModel.SentItemsViewModelFactory
import com.kapilguru.student.databinding.FragmentSentItemsBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.announcement.viewModel.AnnouncementViewModel

class SentItemsFragment : Fragment() {
    private val TAG = "SentItemsFragment"
    val viewModel: AnnouncementViewModel by activityViewModels()
    lateinit var binding: FragmentSentItemsBinding
    lateinit var adapter: SentItemsAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        getSentItemsResponse()
        observeViewModelData()
    }

    fun getSentItemsResponse() {
        val userId: String = StorePreferences(requireContext()).studentId.toString()
        viewModel.getSentItemsResponse(userId)
    }

    fun observeViewModelData() {
        viewModel.sentItemsResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val response = it.data?.data
                    adapter.setData(response as ArrayList<SentItemsData>)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sent_items, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SentItemsAdapter()
        binding.rvSentItems.adapter = adapter
    }
}