package com.kapilguru.student.announcement.newMessage

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.*
import com.kapilguru.student.announcement.newMessage.data.*
import com.kapilguru.student.databinding.FragmentNewMessageBinding
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.announcement.viewModel.AnnouncementViewModel

class NewMessageFragment : Fragment() {
    val TAG = "NewMessageFragment"
    val viewModel: AnnouncementViewModel by activityViewModels()
    lateinit var binding: FragmentNewMessageBinding
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        observeViewModelData()
        viewModel.getAdminList()
    }

    private fun observeViewModelData() {
        viewModel.batchListMutLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateBatchSpinner(it)
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.adminListMutLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    populateAdminSpinner(it)
                    getBatchList()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

        viewModel.sendNewMessageResponce.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    clearInputFields()
                    getSentItemsResponse()
                    Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    when (it.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(requireContext())
                        else -> {
                            Toast.makeText(requireContext(), "Add Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    fun getSentItemsResponse() {
        val userId: String = StorePreferences(requireContext()).studentId.toString()
        viewModel.getSentItemsResponse(userId)
    }

    private fun populateBatchSpinner(it: ApiResource<NewMessageResponse>) {
        val list: List<NewMessageData>? = it.data?.data
        val batchAdapter = ArrayAdapter<NewMessageData>(requireContext(), android.R.layout.simple_spinner_item, list as MutableList<NewMessageData>)
        batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBatch.adapter = batchAdapter
        binding.spinnerBatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selectedBatchReceiverId.value = list[position].batchId.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun populateAdminSpinner(apiResource: ApiResource<AdminMessageResponse>) {
        val data = apiResource.data?.data
        val list: ArrayList<AdminMessageData> = ArrayList()
        if (data!!.isNotEmpty()) {
            list.add(data[0])
            val batchAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list as MutableList<AdminMessageData>)
            batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerBatch.adapter = batchAdapter
            binding.spinnerBatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.selectedAdminUserId.value = list[position].userId.toString()
                    System.out.println("AdminId: " + viewModel.selectedAdminUserId.value)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

    }

    /*Calling getBatchList only after adminList,
    As the last called api is populating the batch spinner(To Trainer is selected by default)*/
    private fun getBatchList() {
        val userId = StorePreferences(requireContext()).studentId.toString()
        viewModel.getBatchList(userId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_message, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.tvSend.setOnClickListener {
            sendMessageRequest()
        }
        binding.tvCancel.setOnClickListener { clearInputFields() }

        binding.radioGroup.setOnCheckedChangeListener { checkedButton, checkedId ->
            if (checkedId == R.id.batch_checkBox) {
                viewModel.batchListMutLiveData.value?.let { populateBatchSpinner(it) }
            } else if (checkedId == R.id.admin_checkBox) {
                viewModel.adminListMutLiveData.value?.let {
                    populateAdminSpinner(it)
                }
            }
        }
    }

    private fun clearInputFields() {
        binding.etSubject.setText("")
        binding.etMessage.setText("")
    }

    private fun sendMessageRequest() {
        Log.d(TAG, "setClickListeners")
        var receiverType = ""
        val senderId: String = StorePreferences(requireContext()).studentId.toString()
        val subject = viewModel.subjectMutLiveData.value!!
        val message = viewModel.messageMutLiveData.value!!
        if (TextUtils.isEmpty(subject) && TextUtils.isEmpty(message)) {
            showToast("Please enter subject and message")
        } else if (TextUtils.isEmpty(subject)) {
            showToast("Please enter subject")
        } else if (TextUtils.isEmpty(message)) {
            showToast("Please enter message")
        } else {
            if (viewModel.isAdminChecked.value!!) { // A = Admin
                val userId = viewModel.selectedAdminUserId.value!!
                receiverType = "A"
                val adminRequest = SendAdminMessageRequest(userId, senderId, String.convertStringToBase64(subject), String.convertStringToBase64(message), receiverType, "U")
                viewModel.sendAdminRequest(adminRequest)
            } else { // B = batch
                val receiverId = viewModel.selectedBatchReceiverId.value!!
                receiverType = "B"
                val batchRequest = SendNewMessageRequest(receiverId, senderId, String.convertStringToBase64(subject), String.convertStringToBase64(message), receiverType, "U")
                viewModel.sendBatchRequest(batchRequest)
            }
        }
    }


    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
}