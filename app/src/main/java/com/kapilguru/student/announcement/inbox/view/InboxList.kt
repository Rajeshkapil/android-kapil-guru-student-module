package com.kapilguru.student.announcement.inbox.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.R
import com.kapilguru.student.announcement.inbox.InboxListTOAdapters
import com.kapilguru.student.announcement.inbox.InboxRecyclerAdapter
import com.kapilguru.student.announcement.inbox.data.InboxItem
import com.kapilguru.student.announcement.inbox.viewModel.InboxViewModel
import com.kapilguru.student.announcement.inbox.viewModel.InboxViewModelFactory
import com.kapilguru.student.databinding.FragmentInboxBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.announcement.viewModel.AnnouncementViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InboxList : Fragment(), InboxListTOAdapters {

    val viewModel: AnnouncementViewModel by activityViewModels()
    lateinit var binding: FragmentInboxBinding
    lateinit var adapter: InboxRecyclerAdapter
    lateinit var dialog: CustomProgressDialog

    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        dialog = CustomProgressDialog(requireContext())
        getInBoxResponse()
        observeViewModelData()
    }

    fun getInBoxResponse(){
        val userId : String = StorePreferences(requireContext()).studentId.toString()
        viewModel.getInboxResponce(userId)
    }

    private fun observeViewModelData(){
        viewModel.resultDat.observe(this, Observer {

            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val response = it.data?.data
                    adapter.setData(response as ArrayList<InboxItem>)
                    if(response.isNotEmpty()) response[0].id?.let { it1 -> viewModel.updateLastMessageId(it1) }
                }
               Status.ERROR -> {
                   dialog.dismissLoadingDialog()
               }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox,container,false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = InboxRecyclerAdapter(this as InboxListTOAdapters)
        binding.rvInbox.adapter = adapter
    }

    override fun getInboxList(inboxItem: InboxItem) {
        TODO("Not yet implemented")
    }
}